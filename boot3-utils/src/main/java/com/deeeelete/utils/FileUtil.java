package com.deeeelete.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;



public class FileUtil {
    public static final String FILE_SEPARATOR;

    public FileUtil() {
    }

    public static String getRealFilePath(String path) {
        return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
    }

    public static String getHttpURLPath(String path) {
        return path.replace("\\", "/");
    }

    public static String inputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder sb = new StringBuilder();

        String s;
        while((s = reader.readLine()) != null) {
            sb.append(s);
        }

        reader.close();
        String str = sb.toString();
        return str;
    }

    public static String changePath(String origPath) {
        return origPath.replaceAll("\\\\", "/");
    }

    public static String ToWav(String webroot, String sourcePath) {
        webroot = "F://ffmpegwin64";
        String[] a = sourcePath.split("\\.");
        String targetPath = a[0] + ".wav";
        Runtime run = null;

        try {
            run = Runtime.getRuntime();
            long start = System.currentTimeMillis();
            Process p = run.exec(webroot + "/bin/ffmpeg -i " + sourcePath + " " + targetPath);
            p.getOutputStream().close();
            p.getInputStream().close();
            p.getErrorStream().close();
            p.waitFor();
            long var8 = System.currentTimeMillis();
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            run.freeMemory();
        }

        return targetPath;
    }

    public static String fileType(String fileName) {
        if (fileName == null) {
            fileName = "文件名为空！";
            return fileName;
        } else {
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
            String[] img = new String[]{"bmp", "jpg", "jpeg", "png", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd", "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf"};

            for(int i = 0; i < img.length; ++i) {
                if (img[i].equals(fileType)) {
                    return "图片";
                }
            }

            String[] document = new String[]{"txt", "doc", "docx", "xls", "htm", "html", "jsp", "rtf", "wpd", "pdf", "ppt"};

            for(int i = 0; i < document.length; ++i) {
                if (document[i].equals(fileType)) {
                    return "文档";
                }
            }

            String[] video = new String[]{"mp4", "avi", "mov", "wmv", "asf", "navi", "3gp", "mkv", "f4v", "rmvb", "webm"};

            for(int i = 0; i < video.length; ++i) {
                if (video[i].equals(fileType)) {
                    return "视频";
                }
            }

            String[] music = new String[]{"mp3", "wma", "wav", "mod", "ra", "cd", "md", "asf", "aac", "vqf", "ape", "mid", "ogg", "m4a", "vqf"};

            for(int i = 0; i < music.length; ++i) {
                if (music[i].equals(fileType)) {
                    return "音频";
                }
            }

            return "其它";
        }
    }

    public static File multipartFileToFile(MultipartFile multipartFile, String savePath) throws IOException {
        File file = new File(savePath);
        OutputStream output = new FileOutputStream(file);
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
        bufferedOutput.write(multipartFile.getBytes());
        bufferedOutput.flush();
        bufferedOutput.close();
        return file;
    }

    public static void writeZip(File file, String parentPath, ZipOutputStream zos) {
        int len;
        if (file.isDirectory()) {
            parentPath = parentPath + file.getName() + File.separator;
            File[] files = file.listFiles();
            File[] var4 = files;
            int var5 = files.length;

            for(len = 0; len < var5; ++len) {
                File f = var4[len];
                writeZip(f, parentPath, zos);
            }
        } else {
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                Throwable var20 = null;

                try {
                    ZipEntry zipEntry = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(zipEntry);
                    byte[] buffer = new byte[10240];

                    while((len = bis.read(buffer, 0, buffer.length)) != -1) {
                        zos.write(buffer, 0, len);
                        zos.flush();
                    }
                } catch (Throwable var16) {
                    var20 = var16;
                    throw var16;
                } finally {
                    if (bis != null) {
                        if (var20 != null) {
                            try {
                                bis.close();
                            } catch (Throwable var15) {
                                var20.addSuppressed(var15);
                            }
                        } else {
                            bis.close();
                        }
                    }

                }
            } catch (Exception var18) {
                var18.printStackTrace();
                throw new RuntimeException(var18.getMessage(), var18.getCause());
            }
        }

    }

    public static void compressToZip(String sourceFilePath, String zipFilePath, String zipFilename) {
        File sourceFile = new File(sourceFilePath);
        File zipPath = new File(zipFilePath);
        if (!zipPath.exists()) {
            zipPath.mkdirs();
        }

        File zipFile = new File(zipPath + File.separator + zipFilename);

        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
            Throwable var7 = null;

            try {
                writeZip(sourceFile, "", zos);
                boolean var8 = deleteDir(sourceFile);
            } catch (Throwable var17) {
                var7 = var17;
                throw var17;
            } finally {
                if (zos != null) {
                    if (var7 != null) {
                        try {
                            zos.close();
                        } catch (Throwable var16) {
                            var7.addSuppressed(var16);
                        }
                    } else {
                        zos.close();
                    }
                }

            }

        } catch (Exception var19) {
            var19.printStackTrace();
            throw new RuntimeException(var19.getMessage(), var19.getCause());
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();

            for(int i = 0; i < children.length; ++i) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public static void deleteFile(File file) {
        if (file != null && file.exists() && file.isFile()) {
            FileOutputStream outputStream = null;

            try {
                outputStream = new FileOutputStream(file);
                outputStream.write(new byte[1]);
            } catch (Exception var11) {
                var11.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException var10) {
                        var10.printStackTrace();
                    }
                }

                file.delete();
            }

        }
    }

    static {
        FILE_SEPARATOR = File.separator;
    }
}
