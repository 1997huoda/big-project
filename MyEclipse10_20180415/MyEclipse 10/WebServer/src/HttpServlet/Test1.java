package HttpServlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class Test1 extends HttpServlet {

	 private String message;
	  private static final long serialVersionUID = 1L;
	  
	/**
	 * Constructor of the object.
	 */
	public Test1() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doDelete method of the servlet. <br>
	 *
	 * This method is called when a HTTP delete request is received.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
//		 // 设置响应内容类型
//	      response.setContentType("text/html");

	      // 实际的逻辑是在这里
//	      PrintWriter out = response.getWriter();
	      

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
//		 String title = "使用 GET 方法读取表单数据";
//	        // 处理中文
//	        String name =new String(request.getParameter("name").getBytes("ISO8859-1"),"UTF-8");
//	        String docType = "<!DOCTYPE html> \n";
//	        out.println(docType +
//	            "<html>\n" +
//	            "<head><title>" + title + "</title></head>\n" +
//	            "<body bgcolor=\"#f0f0f0\">\n" +
//	            "<h1 align=\"center\">" + title + "</h1>\n" +
//	            "<ul>\n" +
//	            "  <li><b>站点名</b>："
//	            + name + "\n" +
//	            "  <li><b>网址</b>："
//	            + request.getParameter("url") + "\n" +
//	            "</ul>\n" +
//	            "</body></html>");
//	    
	    
		
		out.println("<h1>" + message + "</h1>");
		
		
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
//	public void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//		response.setContentType("text/html");
//		PrintWriter out = response.getWriter();
//		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
//		out.println("<HTML>");
//		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
//		out.println("  <BODY>");
//		out.print("    This is ");
//		out.print(this.getClass());
//		out.println(", using the POST method");
//		out.println("  </BODY>");
//		out.println("</HTML>");
//		out.flush();
//		out.close();
//	}

	  protected void doPost(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException {

	        response.setCharacterEncoding("utf-8");

	        DiskFileItemFactory factory = new DiskFileItemFactory();
	        String fileName = "";
	        // 获取文件上传需要保存的路径，upload文件夹需存在。
	        //这是我本地用以存储图片的文件夹路劲
	        String path = "F:\\Workspaces\\img";
	        // 设置暂时存放文件的存储室，这个存储室可以和最终存储文件的文件夹不同。因为当文件很大的话会占用过多内存所以设置存储室。
	        factory.setRepository(new File(path));
	        // 设置缓存的大小，当上传文件的容量超过缓存时，就放到暂时存储室。
	        factory.setSizeThreshold(1024 * 1024);
	        // 上传处理工具类（高水平API上传处理？）
	        ServletFileUpload upload = new ServletFileUpload(factory);

	        try {
	            // 调用 parseRequest（request）方法 获得上传文件 FileItem 的集合list 可实现多文件上传。
	            List<FileItem> list = (List<FileItem>) upload.parseRequest(request);
	            for (FileItem item : list) {
	                // 获取表单属性名字。
	                String name = item.getFieldName();

	                // 如果获取的表单信息是普通的文本信息。即通过页面表单形式传递来的字符串。
	                if (item.isFormField()) {
	                    // 获取用户具体输入的字符串，
	                    String value = item.getString();
	                    request.setAttribute(name, value);
	                }
	                // 如果传入的是非简单字符串，而是图片，音频，视频等二进制文件。
	                else {
	                    // 获取路径名
	                    String value = item.getName();
	                    // 取到最后一个反斜杠。
	                    int start = value.lastIndexOf("\\");
	                    // 截取上传文件的 字符串名字。+1是去掉反斜杠。
	                    String filename = value.substring(start + 1);
	                    request.setAttribute(name, filename);

	                    /*
	                     * 第三方提供的方法直接写到文件中。 item.write(new File(path,filename));
	                     */
	                    // 收到写到接收的文件中。
	                    OutputStream out = new FileOutputStream(new File(path,
	                            filename));
	                    System.out.println("..........."+filename);
	                    fileName = filename;
	                    InputStream in = item.getInputStream();

	                    int length = 0;
	                    byte[] buf = new byte[1024];

	                    while ((length = in.read(buf)) != -1) {
	                        out.write(buf, 0, length);
	                    }
	                    in.close();
	                    out.close();
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	    }
	/**
	 * The doPut method of the servlet. <br>
	 *
	 * This method is called when a HTTP put request is received.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Put your code here
	}

	/**
	 * Returns information about the servlet, such as 
	 * author, version, and copyright. 
	 *
	 * @return String information about this servlet
	 */
	public String getServletInfo() {
		return "This is my default servlet created by Eclipse";
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
		
		 message = "Hello World";
	}

}
