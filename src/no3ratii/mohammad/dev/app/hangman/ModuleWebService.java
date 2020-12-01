package no3ratii.mohammad.dev.app.hangman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;


public class ModuleWebService {

    private static final int         CLIENT_PROTOCOL_EXCEPTION = -1;
    private static final int         IO_EXCEPTION              = -2;
    private static final int         EXCEPTION                 = -404;

    private String                   url;
    private String                   cacheFileName;
    private int                      connectionTimeOut;
    private int                      socketTimeOut;
    private int                      cacheExpiryTime;
    private Listener                 listener;
    private boolean                  cacheData;
    private String                   cacheDiractory;
    private ArrayList<NameValuePair> inputArguments;


    public static interface Listener {

        public void onDataRecive(String valzue);


        public void onFail(int code);
    }


    /*
     * 
     */
    public ModuleWebService listener(Listener value) {
        listener = value;
        return this;
    }


    public ModuleWebService url(String Value) {
        url = Value;
        return this;
    }


    public ModuleWebService cacheData(boolean value) {
        cacheData = value;
        return this;
    }


    /**
     * Examle Value for Set directory
     * <b> filePath / your directory</b>
     */
    public ModuleWebService cacheDiractory(String value) {
        cacheDiractory = value;
        return this;
    }


    /**
     * @value ArrayList 'NameValuePair' params = new ArrayList'NameValuePair'(); </br>
     *        Example: <b>params.add(new BasicNameValuePair("name2", "mohammad"));</b>
     */
    public ModuleWebService inputArguments(ArrayList<NameValuePair> params) {
        inputArguments = params;
        return this;
    }


    public ModuleWebService cacheExpiryTime(int value) {
        cacheExpiryTime = value;
        return this;
    }


    public ModuleWebService connectionTimeOut(int value) {
        connectionTimeOut = value;
        return this;
    }


    public ModuleWebService socketTimeOut(int value) {
        socketTimeOut = value;
        return this;
    }


    public void read() {
        String data = null;
        if (cacheData) {
            cacheFileName = createCacheFileName();
            data = readFromCache();
        }
        if (data == null) {
            readFromServer();
        } else {
            listener.onDataRecive(data);//<------------------problem
        }
    }


    private void readFromServer() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpResponse response = httpclient.execute(new HttpPost(url));
                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeOut);
                    HttpConnectionParams.setSoTimeout(httpParameters, socketTimeOut);
                    InputStream inputStream = response.getEntity().getContent();
                    String data = convertStreamToString(inputStream);
                    long newTime = System.currentTimeMillis();
                    if (data != null) {
                        listener.onDataRecive(data);
                    }
                    if (cacheData) {
                        writeToCache(newTime, data);
                    }
                }
                catch (ClientProtocolException e) {
                    listener.onFail(CLIENT_PROTOCOL_EXCEPTION);
                }
                catch (IOException e) {
                    listener.onFail(IO_EXCEPTION);
                }
                catch (Exception e) {
                    listener.onFail(EXCEPTION);
                }
            }
        });
        thread.start();
    }


    private String createCacheFileName() {
        String output = url;
        if (inputArguments != null) {
            for (NameValuePair params: inputArguments) {
                output += "|" + params.getName() + ":" + params.getValue();
            }
            // output :http:192.168.1.2/webService|name:mohammad
            return sha1(output) + ".mono";
        }
        return sha1(url) + ".mono";

    }


    private void writeToCache(long when, String value) {
        ObjectOutputStream outputStream = null;

        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(cacheDiractory + "/" + cacheFileName));
            int valueLength = value.length();
            outputStream.writeLong(when);
            outputStream.writeInt(valueLength);
            outputStream.write(value.getBytes());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String readFromCache() {
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(cacheDiractory + "/" + cacheFileName));
            long when = inputStream.readLong();
            long newTime = System.currentTimeMillis();
            if (newTime - when > cacheExpiryTime * 1000) {
                new File(cacheDiractory + "/" + cacheFileName).delete();
                return null;
            }
            int valueLenght = inputStream.readInt();
            byte[] buffer = new byte[valueLenght];
            inputStream.read(buffer, 0, valueLenght);
            String outPut = new String(buffer);
            return outPut;
        }
        catch (StreamCorruptedException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    private String convertStreamToString(InputStream inputStream) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder bilder = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                bilder.append(line + "\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bilder.toString();
    }


    private String sha1(String text) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] textBytes = text.getBytes("iso-8859-1");
            md.update(textBytes, 0, textBytes.length);
            byte[] sha1hash = md.digest();
            return convertToHex(sha1hash);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b: data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            }
            while (two_halfs++ < 1);
        }
        return buf.toString();
    }

}
