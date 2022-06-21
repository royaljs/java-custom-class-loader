package dev.jiwonlee;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomClassLoader extends ClassLoader {

    private Map<String, String> classFileMap = new HashMap<String, String>();

    public CustomClassLoader(ClassLoader parentClassloader, List<String> classPaths) throws IOException {
        super(parentClassloader);

        for (String classPath : classPaths) {
            File f = new File(classPath);
            if (!f.exists())
                throw new FileNotFoundException(String.format("Class File Not Found: %s", f.getAbsolutePath()));
            System.out.println(f.getName());
            classFileMap.put(f.getName(), f.getAbsolutePath());
            System.out.println(String.format("Class Name: %s, Class Path: %s", f.getName(), f.getAbsolutePath()));
            /**
             * TODO: find and add all classes in class paths.
             */
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println(String.format("Find class: %s", name));
        String simpleClassName = getSimpleClassName(name);

        if (!classFileMap.containsKey(simpleClassName))
            throw new ClassNotFoundException(String.format("Class Not Found: %s", name));

        try {
            byte[] b = loadClassFromFile(classFileMap.get(simpleClassName));
            return defineClass(name, b, 0, b.length);
        } catch (Exception e) {
            throw new ClassNotFoundException(String.format("Class Not Found: %s", name), e);
        }
    }

    private String getSimpleClassName(String name) {
        return name.substring(name.lastIndexOf('.') + 1);
    }

    private byte[] loadClassFromFile(String path) throws IOException {
        File f = new File(path);
        try (InputStream inputStream = new FileInputStream(f.getAbsolutePath())) {
            byte[] buffer;
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int nextValue = 0;

            while ((nextValue = inputStream.read()) != -1) {
                byteStream.write(nextValue);
            }
            buffer = byteStream.toByteArray();
            return buffer;
        }
    }

}