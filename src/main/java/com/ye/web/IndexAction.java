package com.ye.web;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.ye.util.UploadDownloadUtil;
import freemarker.template.Configuration;
import org.apache.struts2.ServletActionContext;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sh1 on 16-8-27.
 */
public class IndexAction extends ActionSupport implements
        ModelDriven, Preparable {
    private IndexModel model;
    private HttpServletRequest request = ServletActionContext.getRequest();

    public IndexAction() {
        this.model = new IndexModel();
    }

    public String toIndex() throws Exception {
        return "index";
    }

    public String exportPdf() {
        return "exportPdf";
    }

    public String getExportPdfFileName() {
        return UploadDownloadUtil.toAttachmentFileName("简历.pdf", request);
    }


    public InputStream getExportPdfStream() throws Exception {
        String basePath = request.getSession().getServletContext().getRealPath("/");
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(new File(basePath + "/index"));
        cfg.setDefaultEncoding("UTF-8");
        freemarker.template.Template temp = cfg.getTemplate("index.ftl");
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("name", model.getName());
        String htmlId = UUID.randomUUID().toString().replace("-", "");
        File file = new File("F:\\project\\" + htmlId + ".html");
        if (!file.exists()) {
            file.createNewFile();
        }

        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), "utf-8"));
        temp.process(root, out);
        out.flush();

        String url = file.toURI().toURL().toString();
        String outputFile = "F:\\project\\" + htmlId + ".pdf";
        OutputStream os = new FileOutputStream(outputFile);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(url);

        // 解决中文问题
        ITextFontResolver fontResolver = renderer.getFontResolver();
        try {
            URL resource = Thread.currentThread().getContextClassLoader().getResource("simsun.ttc");
            assert resource != null;
            fontResolver.addFont(resource.toString(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        renderer.layout();
        try {
            renderer.createPDF(os);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        os.close();
        return new FileInputStream(outputFile);
    }

    @Override
    public IndexModel getModel() {
        return model;
    }

    @Override
    public void prepare() throws Exception {

    }
}
