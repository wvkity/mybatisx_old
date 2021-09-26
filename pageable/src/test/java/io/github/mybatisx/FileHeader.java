package io.github.mybatisx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 *
 */
public class FileHeader {

    private static final Logger log = LoggerFactory.getLogger(FileHeader.class);

    public static enum FileCategory {
        IMAGE, VIDEO, DOCUMENT
    }

    public static enum FileType {
        JPEG(FileCategory.IMAGE, "FFD8FF", "^FFD8FF(.*)?"),
        PNG(FileCategory.IMAGE, "89504E47", "^89504E47(.*)?"),
        BMP(FileCategory.IMAGE, "", ""),
        PST(FileCategory.DOCUMENT, "", ""),
        XLS_DOC(FileCategory.DOCUMENT, "", ""),
        XLSX_DOCX(FileCategory.DOCUMENT, "", ""),
        WPS(FileCategory.DOCUMENT, "", ""),
        PDF(FileCategory.DOCUMENT, "", ""),
        AVI(FileCategory.VIDEO, "", ""),
        RAM(FileCategory.VIDEO, "", ""),
        RM(FileCategory.VIDEO, "", ""),
        MPG(FileCategory.VIDEO, "", ""),
        MOV(FileCategory.VIDEO, "", ""),
        ASF(FileCategory.VIDEO, "", ""),
        MP4(FileCategory.VIDEO, "00000020667479706D70", "^0{6}([a-z0-9]{2})667479706(.*)?"),
        FLV(FileCategory.VIDEO, "", ""),
        MID(FileCategory.VIDEO, "", "");

        final FileCategory category;
        final String signature;
        final String regex;

        FileType(FileCategory category, String signature, String regex) {
            this.category = category;
            this.signature = signature;
            this.regex = regex;
        }

        public FileCategory getCategory() {
            return category;
        }

        public String getSignature() {
            return signature;
        }

        public String getRegex() {
            return regex;
        }

        @Override
        public String toString() {
            return this.name() + "{" +
                "category=" + category +
                ", signature='" + signature + '\'' +
                ", regex='" + regex + '\'' +
                '}';
        }
    }

    public static class Matcher {
        private final boolean matches;
        private final String headHex;
        private final FileType fileType;

        public Matcher(boolean matches, String headHex, FileType fileType) {
            this.matches = matches;
            this.headHex = headHex;
            this.fileType = fileType;
        }

        public boolean isMatches() {
            return matches;
        }

        public String getHeadHex() {
            return headHex;
        }

        public FileType getFileType() {
            return fileType;
        }

        @Override
        public String toString() {
            return "Matcher{" +
                "matches=" + matches +
                ", headHex='" + headHex + '\'' +
                ", fileType=" + fileType +
                '}';
        }
    }

    private static final Map<FileType, Pattern> FILE_REGEX_CACHE = new ConcurrentHashMap<>();

    /**
     * 解析文件头
     * @param filePath 文件路径
     * @return 文件头信息
     * @throws IOException IO异常
     */
    public String parse(final String filePath) throws IOException {
        return this.parse(new File(filePath));
    }

    /**
     * 解析文件头
     * @param file 文件对象
     * @return 文件头信息
     * @throws IOException IO异常
     */
    public String parse(final File file) throws IOException {
        return this.parse(new FileInputStream(file), true);
    }

    /**
     * 解析文件头
     * @param input       输入溜
     * @param isAutoClose 是否自动关闭输入流
     * @return 文件头信息
     * @throws IOException IO异常
     */
    public String parse(final InputStream input, final boolean isAutoClose) throws IOException {
        if (Objects.nonNull(input)) {
            try {
                final byte[] bytes = new byte[10];
                final int read = input.read(bytes, 0, bytes.length);
                if (read > 0) {
                    return this.toHex(bytes);
                }
            } finally {
                if (isAutoClose) {
                    try {
                        input.close();
                    } catch (Exception ignore) {
                        // ignore
                    }
                }
            }
        }
        return null;
    }

    /**
     * 根据网络地址解析文件头
     * @param netUrl 网络地址
     * @return 文件头
     * @throws Exception 异常
     */
    public String netParse(final String netUrl) throws Exception {
        this.trustHosts();
        final URLConnection url = new URL(netUrl).openConnection();
        url.connect();
        return this.parse(url.getInputStream(), true);
    }

    private void trustHosts() {
        final TrustManager[] managers = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    // empty
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    // empty
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
        };
        try {
            final SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, managers, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            log.warn("TLS证书异常: ", e);
        }
    }

    /**
     * 字节数组转16进制字符串
     * @param sources 待转换字节数组
     * @return 16进制字符串
     */
    public String toHex(final byte[] sources) {
        if (sources == null || sources.length == 0) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        for (byte src : sources) {
            final int v = src & 0xFF;
            final String hex = Integer.toHexString(v).toUpperCase(Locale.ENGLISH);
            if (hex.length() < 2) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 获取文件类型
     * @param filePath 文件路径
     * @return {@link Matcher}
     * @throws IOException IO异常
     */
    public Matcher matches(final String filePath) throws IOException {
        return this.matches(new File(filePath));
    }

    /**
     * 获取文件类型
     * @param file {@link File}
     * @return {@link Matcher}
     * @throws IOException IO异常
     */
    public Matcher matches(final File file) throws IOException {
        return this.matches(new FileInputStream(file), true);
    }

    /**
     * 获取文件类型
     * @param input       输入流
     * @param isAutoClose 是否自动关闭流
     * @return {@link FileType}
     * @throws IOException IO异常
     */
    public Matcher matches(final InputStream input, final boolean isAutoClose) throws IOException {
        return this.matchesOfHex(this.parse(input, isAutoClose));
    }

    /**
     * 根据网络地址获取文件类型
     * @param netUrl 网络地址
     * @return {@link FileType}
     * @throws Exception 异常
     */
    public Matcher matchesOfNet(final String netUrl) throws Exception {
        return this.matchesOfHex(this.netParse(netUrl));
    }

    /**
     * 根据文件头获取文件类型
     * @param fileHeadHex 文件头16进制字符串
     * @return {@link FileType}
     */
    public Matcher matchesOfHex(final String fileHeadHex) {
        if (fileHeadHex != null && !fileHeadHex.trim().isEmpty()) {
            final FileType[] items = FileType.values();
            final String headHex = fileHeadHex.toUpperCase(Locale.ENGLISH);
            for (FileType it : items) {
                if (this.isNotEmpty(it.getSignature()) && headHex.startsWith(it.getSignature())) {
                    return new Matcher(true, fileHeadHex, it);
                } else if (this.isNotEmpty(it.getRegex())) {
                    Pattern regex = FILE_REGEX_CACHE.get(it);
                    if (regex == null) {
                        regex = Pattern.compile(it.getRegex(), Pattern.CASE_INSENSITIVE);
                        FILE_REGEX_CACHE.putIfAbsent(it, regex);
                    }

                    if (regex.matcher(fileHeadHex).matches()) {
                        return new Matcher(true, headHex, it);
                    }
                }
            }
        }
        return new Matcher(false, fileHeadHex, null);
    }

    private byte[] readByte(final InputStream is) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(is);
             ByteArrayOutputStream bas = new ByteArrayOutputStream(4096);
             BufferedOutputStream bos = new BufferedOutputStream(bas)) {
            byte[] bytes = new byte[4096];
            int size;
            while ((size = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, size);
            }
            bos.flush();
            return bas.toByteArray();
        }
    }

    private boolean isNotEmpty(final String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static void main(String[] args) {
        try {
            /*final FileHeader fh = new FileHeader();
            final String f1 = "/Users/mddxhmb/Desktop/vv/480.mp4";
            final Matcher mp4 = fh.matches(f1);
            log.info("mp4 file: {}, {}, {}", mp4.isMatches(), mp4.getHeadHex(), mp4.getFileType());
            final String netUrl = "http://img.daimg.com/uploads/allimg/141028/3-14102R33154.jpg";
            log.info("网络图片: {}", fh.matchesOfNet(netUrl));*/

            final File file = new File("/Users/mddxhmb/Desktop/test.jpeg");
            final FileInputStream fis = new FileInputStream(file);
            final BufferedInputStream bis = new BufferedInputStream(fis);
            final boolean isMark = bis.markSupported();
            if (isMark) {
                bis.mark(20);
            }
            final FileHeader fh = new FileHeader();
            final String f1 = "/Users/mddxhmb/Desktop/test.mp4";
            final Matcher mp4 = fh.matches(f1);
            log.info("mp4 file: {}, {}, {}", mp4.isMatches(), mp4.getHeadHex(), mp4.getFileType());
            /*log.info("file: {}", fh.matches(bis, false));
            if (isMark) {
                bis.reset();
            }
            final FileOutputStream fos = new FileOutputStream("/Users/mddxhmb/Desktop/test2.jpeg");
            final BufferedOutputStream bos = new BufferedOutputStream(fos);
            byte[] read = new byte[4096];
            while (bis.read(read) != -1 ) {
                bos.write(read);
            }
            bos.flush();
            fos.flush();*/
        } catch (Exception e) {
            log.error("文件头解析失败: ", e);
        }
    }
}
