import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
    private final Set<String> visitedUrls = new HashSet<>();
    private final LinkedBlockingQueue<String> urlQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executorService;
    private static final int THREAD_COUNT = 5;
    private static final Pattern URL_PATTERN = Pattern.compile("https?://\\S+");

    public WebCrawler() {
        executorService = Executors.newFixedThreadPool(THREAD_COUNT);
    }

    public void startCrawling(String startUrl) {
        urlQueue.add(startUrl);
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.execute(this::processQueue);
        }
    }

    private void processQueue() {
        while (!urlQueue.isEmpty()) {
            try {
                String url = urlQueue.poll();
                if (url != null && !visitedUrls.contains(url)) {
                    visitedUrls.add(url);
                    System.out.println("Crawling: " + url);
                    String content = fetchContent(url);
                    extractAndQueueUrls(content);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
    }

    private String fetchContent(String urlString) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Failed to fetch: " + urlString);
        }
        return content.toString();
    }

    private void extractAndQueueUrls(String content) {
        Matcher matcher = URL_PATTERN.matcher(content);
        while (matcher.find()) {
            String foundUrl = matcher.group();
            if (!visitedUrls.contains(foundUrl)) {
                urlQueue.add(foundUrl);
            }
        }
    }

    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler();
        crawler.startCrawling("https://example.com");
    }
}
