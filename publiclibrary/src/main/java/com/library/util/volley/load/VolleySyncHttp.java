package com.library.util.volley.load;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.library.util.volley.FastStringRequest;

import java.util.Map;

/**
 * Created by DaiYao on 2016/9/26.
 *  VolleySyncHttp volleySyncHttp = VolleySyncHttp.getInstance();
     try
     {
         RequestFuture<String> future1 = volleySyncHttp.syncGet(mQueue, url2);
         String result1 = future1.get();
         Log.e(TAG, "future1: " + result1);
     } catch (InterruptedException e)
     {
        e.printStackTrace();
     } catch (ExecutionException e)
     {
        e.printStackTrace();
     }
 */
public class VolleySyncHttp
{
    private static VolleySyncHttp mVolleySyncHttp;

    public static VolleySyncHttp getInstance()
    {
        if (mVolleySyncHttp == null)
        {
            mVolleySyncHttp = new VolleySyncHttp();
        }
        return mVolleySyncHttp;
    }

    public RequestFuture<String> syncGet(RequestQueue mQueue, String url)
    {
        return syncGet(mQueue, url, null, "sync");
    }

    public RequestFuture<String> syncGet(RequestQueue mQueue, String url, Map<String, String> mParams,String tag)
    {
        RequestFuture<String> futureA = RequestFuture.newFuture();

        ResponseResult responseResult = new ResponseResult(futureA);
        ResponseError responseError = new ResponseError(futureA);
        FastStringRequest request = new FastStringRequest
                (
                        Request.Method.GET,
                        url,
                        mParams,
                        responseResult,
                        responseError
                );
        request.setTag(tag);
        futureA.setRequest(request);
        mQueue.add(request);

        return futureA;
    }

    class ResponseResult implements Response.Listener<String>
    {
        private RequestFuture<String> futureA;

        public ResponseResult(RequestFuture<String> futureA)
        {
            this.futureA = futureA;
        }

        @Override
        public void onResponse(String response)
        {
            futureA.onResponse(response);
        }
    }

    class ResponseError implements Response.ErrorListener
    {
        private RequestFuture<String> futureA;

        public ResponseError(RequestFuture<String> futureA)
        {
            this.futureA = futureA;
        }

        @Override
        public void onErrorResponse(VolleyError error)
        {
            futureA.onErrorResponse(error);
        }
    }
}
