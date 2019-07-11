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
		
//		 // ������Ӧ��������
//	      response.setContentType("text/html");

	      // ʵ�ʵ��߼���������
//	      PrintWriter out = response.getWriter();
	      

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
//		 String title = "ʹ�� GET ������ȡ������";
//	        // ��������
//	        String name =new String(request.getParameter("name").getBytes("ISO8859-1"),"UTF-8");
//	        String docType = "<!DOCTYPE html> \n";
//	        out.println(docType +
//	            "<html>\n" +
//	            "<head><title>" + title + "</title></head>\n" +
//	            "<body bgcolor=\"#f0f0f0\">\n" +
//	            "<h1 align=\"center\">" + title + "</h1>\n" +
//	            "<ul>\n" +
//	            "  <li><b>վ����</b>��"
//	            + name + "\n" +
//	            "  <li><b>��ַ</b>��"
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
	        // ��ȡ�ļ��ϴ���Ҫ�����·����upload�ļ�������ڡ�
	        //�����ұ������Դ洢ͼƬ���ļ���·��
	        String path = "F:\\Workspaces\\img";
	        // ������ʱ����ļ��Ĵ洢�ң�����洢�ҿ��Ժ����մ洢�ļ����ļ��в�ͬ����Ϊ���ļ��ܴ�Ļ���ռ�ù����ڴ��������ô洢�ҡ�
	        factory.setRepository(new File(path));
	        // ���û���Ĵ�С�����ϴ��ļ���������������ʱ���ͷŵ���ʱ�洢�ҡ�
	        factory.setSizeThreshold(1024 * 1024);
	        // �ϴ��������ࣨ��ˮƽAPI�ϴ�������
	        ServletFileUpload upload = new ServletFileUpload(factory);

	        try {
	            // ���� parseRequest��request������ ����ϴ��ļ� FileItem �ļ���list ��ʵ�ֶ��ļ��ϴ���
	            List<FileItem> list = (List<FileItem>) upload.parseRequest(request);
	            for (FileItem item : list) {
	                // ��ȡ���������֡�
	                String name = item.getFieldName();

	                // �����ȡ�ı���Ϣ����ͨ���ı���Ϣ����ͨ��ҳ�����ʽ���������ַ�����
	                if (item.isFormField()) {
	                    // ��ȡ�û�����������ַ�����
	                    String value = item.getString();
	                    request.setAttribute(name, value);
	                }
	                // ���������ǷǼ��ַ���������ͼƬ����Ƶ����Ƶ�ȶ������ļ���
	                else {
	                    // ��ȡ·����
	                    String value = item.getName();
	                    // ȡ�����һ����б�ܡ�
	                    int start = value.lastIndexOf("\\");
	                    // ��ȡ�ϴ��ļ��� �ַ������֡�+1��ȥ����б�ܡ�
	                    String filename = value.substring(start + 1);
	                    request.setAttribute(name, filename);

	                    /*
	                     * �������ṩ�ķ���ֱ��д���ļ��С� item.write(new File(path,filename));
	                     */
	                    // �յ�д�����յ��ļ��С�
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
