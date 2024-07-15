package utils;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import objects.Candidate;
import objects.Message;
import server.BasicServer;
import server.ContentType;
import server.Cookie;
import server.ResponseCodes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();
    private final SampleDataModel sampleDataModel = new SampleDataModel();

    public Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/", this::mainHandler);
        registerGet("/votes",this::votesHandler);
        registerPost("/vote",this::voteHandlerPost);
        registerGet("/thankyou",this::thankYouHandler);
    }

    private void thankYouHandler(HttpExchange exchange) {
        String cookiesStr = getCookies(exchange);
        Map<String, String> cookies = Cookie.parse(cookiesStr);
        String candidateId = cookies.get(" candidateId");
        Candidate profile = sampleDataModel.findCandidate(candidateId);
        if (profile != null) {
            renderTemplate(exchange, "thankyou.html", getSampleDataModelWithCandidate(profile));
        } else {
            Message msg = new Message("Error we did not find candidate profile !");
            renderTemplate(exchange, "exception.html", getSampleDataModelMessage(msg));
        }
    }

    private void voteHandlerPost(HttpExchange exchange) {
        String raw = getBody(exchange);
        Map<String, String> parsed = Common.parseUrlEncodedLogin(raw, "&");
        String candidateId = parsed.get("candidateId");
        Cookie candidateIdCookie = Cookie.make("candidateId", candidateId);
        candidateIdCookie.setMaxAge(600);
        candidateIdCookie.setHttpOnly(true);
        setCookies(exchange, candidateIdCookie);
        sampleDataModel.findCandidateAndAddVote(candidateId);
        redirect301(exchange, "/thankyou");
    }

    private void votesHandler(HttpExchange exchange) {
        renderTemplate(exchange, "votes.html", getSampleDataModel());
    }

    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            cfg.setDirectoryForTemplateLoading(new File("data/html"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void mainHandler(HttpExchange exchange) {
        renderTemplate(exchange, "candidates.html", getSampleDataModel());
    }

    protected void renderTemplate(HttpExchange exchange, String templateFile, Object dataModel) {
        try {
            Template temp = freemarker.getTemplate(templateFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try (OutputStreamWriter writer = new OutputStreamWriter(stream)) {
                temp.process(dataModel, writer);
                writer.flush();
                var data = stream.toByteArray();
                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> getSampleDataModel() {
        Map<String, Object> model = new HashMap<>();
        SampleDataModel sampleData = new SampleDataModel();
        model.put("candidates", sampleData.getCandidates());
        return model;
    }

    private Map<String, Object> getSampleDataModelMessage(Message msg) {
        Map<String, Object> model = new HashMap<>();
        model.put("message", msg);
        return model;
    }

    private Map<String, Object> getSampleDataModelWithCandidate(Candidate profile) {
        Map<String, Object> model = new HashMap<>();
        model.put("candidate", profile);
        return model;
    }

}
