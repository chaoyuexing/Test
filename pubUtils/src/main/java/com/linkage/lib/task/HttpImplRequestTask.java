package com.linkage.lib.task;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import com.linkage.lib.model.HttpStringPart;
import com.linkage.lib.util.LogUtils;

import android.text.TextUtils;

public abstract class HttpImplRequestTask<T> extends AbstractAsyncRequestTask<T> {
	
	//放置该http请求中所有字串类的键值对参数
	protected LinkedList<HttpStringPart> mParams;
	//服务器地址
	protected String hostUrl;
	//用于放置post请求body
	private MultipartEntity requestContent; 
	
	/**
	 * @author 方达
	 * @param hostUrl 服务器地址（具体接口名在getRequestInterFace中实现）
	 * @param method 区分Post与Get请求类型
	 * @param mParams 放置该http请求中所有字串类的键值对参数
	 */
	public HttpImplRequestTask(String hostUrl, RequestMethod method, LinkedList<HttpStringPart> mParams) 
	{
		super(hostUrl, method);
		this.hostUrl = hostUrl;
		if (mParams == null) {
			mParams = new LinkedList<HttpStringPart>();
		}
		this.mParams = mParams;
		if (method == RequestMethod.POST) 
		{
			requestContent = new MultipartEntity();
		}
	}
	
	/**
	 * 拼装get请求
	 */
	@Override
	protected final HttpGet getHttpGet() throws Exception {
		handleRequestUrl();
		if(mParams != null) {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (Iterator<HttpStringPart> iterator = mParams.iterator(); iterator.hasNext();) {
				HttpStringPart part = iterator.next();
				if(first) 
				{ 
					first = false;
				} 
				else 
					sb.append("&");
				sb.append(part.getKey() + "=" + part.getValue());
			}
			mRequestUrl = mRequestUrl + "?" + sb.toString();
		}
		LogUtils.i("httpGetRequestUrl:" + mRequestUrl);
		return new HttpGet(mRequestUrl);
	}

	/**
	 * 拼装get请求
	 */
	@Override
	protected final HttpPost getHttpPost() throws Exception 
	{
		handleRequestUrl();
		HttpPost post = new HttpPost(mRequestUrl);
		if (mParams != null && requestContent != null) 
		{
			for (Iterator<HttpStringPart> iterator = mParams.iterator(); iterator.hasNext();) {
				HttpStringPart part = iterator.next();
				try
		        {
					LogUtils.d("PARAM[" + part.getKey() + " : " + part.getValue() + "]");
		            requestContent.addPart(part.getKey(), new StringBody(part.getValue()));
		        }
		        catch (Exception e)
		        {
		            e.printStackTrace();
		        }
			}
		}
		post.setEntity(requestContent);
		return post;
	}
	
	/**
     * 添加请求体文件
     * 
     * @param paramName
     *            参数名
     * @param data
     *            文件数据
     * @param mimeType
     *            文件格式类型
     * @param fileName
     *            文件名
     */
    public void addPostBodyFile(String paramName,
            byte[] data,
            String mimeType,
            String fileName)
    {
    	if (requestContent == null) 
    	{
			throw new RuntimeException("get请求不支持传送文件流！");
		}
        try
        {
            requestContent.addPart(paramName, new ByteArrayBody(data,
                    mimeType,
                    fileName));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 添加请求体文件
     * 
     * @param paramName
     *            参数名
     * @param data
     *            文件数据
     * @param mimeType
     *            文件格式类型
     * @param fileName
     *            文件名
     */
    public void addPostBodyFile(String paramName,
            File file,
            String mimeType)
    {
    	if (requestContent == null) 
    	{
			throw new RuntimeException("get请求不支持传送文件流！");
		}
        try
        {
        	if (file.exists()) 
        	{
        		requestContent.addPart(paramName, new FileBody(file, mimeType));
			} else 
			{
				LogUtils.i("上传路径:" + file.getAbsolutePath() + " 不存在");
			}
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取具体接口名称
     * @return urlInterfaceName
     */
    protected abstract String getRequestInterface();
	
	/**
	 * 处理host与接口名的拼接
	 * 用于某些服务器共用同一接口的处理等
	 */
    protected void handleRequestUrl() {
		String interfaceName = getRequestInterface();
		if (!TextUtils.isEmpty(interfaceName)) {
			if (hostUrl.endsWith("/")) {
				mRequestUrl = hostUrl + interfaceName;
			} else {
				mRequestUrl = hostUrl + "/" + interfaceName;
			}
		}
	}
    
    private class ByteArrayBody extends AbstractContentBody
    {
        private final byte[] bytes;
        private final String fileName;

        public ByteArrayBody(byte[] bytes, String mimeType, String fileName)
        {
            super(mimeType);
            this.bytes = bytes;
            this.fileName = fileName;
        }

        public String getFilename()
        {
            return fileName;
        }

        @Override
        public void writeTo(OutputStream out) throws IOException
        {
            out.write(bytes);
        }

        public String getCharset()
        {
            return null;
        }

        public long getContentLength()
        {
            return bytes.length;
        }

        public String getTransferEncoding()
        {
            return MIME.ENC_BINARY;
        }

    }
}
