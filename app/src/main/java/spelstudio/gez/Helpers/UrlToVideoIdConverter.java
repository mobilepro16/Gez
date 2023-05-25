package spelstudio.gez.Helpers;

public class UrlToVideoIdConverter {
    String videoId = "";

    public UrlToVideoIdConverter() {
    }

    public String videoId(String s) {
        if (s.contains("=")) {
            Boolean found = false;
            int a = 0, b = 0, indexToCopy = 0;
            while (a <= s.length()) {
                if (s.charAt(a) == '=') {
                    indexToCopy = a + 1;
                    videoId = s.substring(indexToCopy, s.length());
                    break;
                }
                a++;
            }
            return videoId;
        }
        return s;
    }
}