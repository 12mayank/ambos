package co.jlabs.famb;


import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by Jlabs-Win on 31/01/2017.
 */

public class MultipartRequest extends Request<JSONObject> {

//    private final Response.Listener<JSONObject> mListener;
//    private final Response.ErrorListener mErrorListener;
//    private final Map<String, String> mHeaders;
//    private final String mMimeType;
//    private final byte[] mMultipartBody;
//    private RequestBody entity;

//    public MultipartRequest(String url, Map<String, String> headers, String mimeType, byte[] multipartBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
//        super(Method.POST, url, errorListener);
//        this.mListener = listener;
//        this.mErrorListener = errorListener;
//        this.mHeaders = headers;
//        this.mMimeType = mimeType;
//        this.mMultipartBody = multipartBody;
//    }
private static final String FILE_PART_NAME   = "file";
    private static final String STRING_PART_NAME = "text";

    private final Response.Listener<JSONObject> mListener;
    private final File mFilePart;
    private final String mStringPart;
    private RequestBody entity;

    public MultipartRequest(String url, Response.ErrorListener errorListener, Response.Listener<JSONObject> listener, File file, String stringPart) {
        super(Method.POST, url, errorListener);

        mListener = listener;
        mFilePart = file;
        mStringPart = stringPart;
        buildMultipartEntity();
    }

    private void buildMultipartEntity() {
        entity = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(STRING_PART_NAME, "file")
                .addFormDataPart(FILE_PART_NAME, mFilePart.getName(),
                        RequestBody.create(MediaType.parse(getMimeType(mFilePart)), mFilePart))
                .build();
    }

    @Override
    public String getBodyContentType() {
       // EruditusLog.d("UT_LOG", entity.contentType() + "");
        return entity.contentType() + "";
    }

//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        return (mHeaders != null) ? mHeaders : super.getHeaders();
//    }

//    @Override
//    public String getBodyContentType() {
//        return mMimeType;
//    }

    @Override
    public byte[] getBody() throws AuthFailureError {
       // return mMultipartBody;
        //for JSON

        BufferedSink bufferedSink;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bufferedSink = Okio.buffer(Okio.sink(bos));
            entity.writeTo(bufferedSink);
            bufferedSink.close();
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//        try {
//            return Response.success(
//                    response,
//                    HttpHeaderParser.parseCacheHeaders(response));
//        } catch (Exception e) {
//            return Response.error(new ParseError(e));
//        }


        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }



    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

//    @Override
//    public void deliverError(VolleyError error) {
//        mErrorListener.onErrorResponse(error);
//    }

    static String getMimeType(@NonNull File file) {
        String type = null;
        final String url = file.toString();
        final String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        if (type == null) {
            type = "image/*"; // fallback type. You might set it to */*
        }
        return type;
    }
}
