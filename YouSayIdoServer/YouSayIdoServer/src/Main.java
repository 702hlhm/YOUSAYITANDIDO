import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Main {
	
	public Vector<SerSession> serSessions;	
	private ServerSocket serverSocket = null;
	
	public Main() {
		try {
			serSessions = new Vector<SerSession>();
			serverSocket = new ServerSocket(8080);
		} catch (IOException e) {
			System.out.println("Can not create a serverSocket");
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		try {
		    System.out.println("Server start");
			Main mServer = new Main();
			mServer.work();			
		} catch (Exception e) {
			System.out.println("运行时崩溃！");
			e.printStackTrace();
		}
	}
	
	public void work() {
		try {
			while (true) {
				Socket socket;
				socket = serverSocket.accept();
				SerSession session = new SerSession(socket);
				serSessions.add(session);
				new Thread(session).start();		
			}
		} catch (Exception e) {
			System.out.println("Fail to create a socket");
		}
	}
	
	public class SerSession implements Runnable {

		public String nameString;
		public String password;
		public String ipString;					
		public Socket sock;
		public Boolean iflogin;
		public Vector<Task> tasks; 
		
		public SerSession(){
			sock = null;
			nameString = "";
			password = "";
			ipString = "";
			iflogin = false;
		}
		public SerSession(Socket socket) {
			sock = socket;
			nameString = "";
			password = "";
			ipString = "";
			iflogin = false;
		}

		@Override
		public void run() {
			try {
		           try {
		        	   while(true){
		              InputStream input = sock.getInputStream();
		              OutputStream output = sock.getOutputStream();
		              
		              String qString = readLine(input);
		              System.out.println(qString);
		              String [] qstStrings = qString.split(" ");
		              if (qstStrings[0].equals("Client")) {
						String reply = "Server " + qstStrings[1] +"\r\n";
						output.write(reply.getBytes());
						
					} else if (qstStrings[1].equals("REGISTER")) {
						Map<String, String> headers = readHeaders(input);
						boolean flag = true;
						if(!serSessions.isEmpty())
						{
						  for (SerSession serSession : serSessions) {
						    	if (serSession.nameString.equals(qstStrings[2])) {
								  flag = false;
							    }
						  }
						}					
						
						if(!flag){
							respondMsg('8');//注册失败
						}						
						else{
							
							nameString = qstStrings[2];
							ipString = sock.getInetAddress().toString().substring(1);
							password = headers.get("Password");
							System.out.println(nameString+"  "+ipString+"  "+password);
							
							respondMsg('7');//注册成功
							 
							serSessions.add(this);
						}
					}else if (qstStrings[1].equals("LOGIN")) {
						Map<String, String> headers = readHeaders(input);
						boolean flag = false;
						if(!serSessions.isEmpty())
						{
						  for (SerSession serSession : serSessions) {
						    	if (serSession.nameString.equals(qstStrings[2])) {
								  if (headers.get("Password").equals(serSession.password)) {
									flag = true;
								}
							    }
						  }
						}					
						
						if(flag){
							respondMsg('5');//登陆成功
							iflogin = true;
						}						
						else{
							respondMsg('6');//登陆失败
						}
						
					}else if (qstStrings[1].equals("LOGINOUT") && iflogin) {
						try {
							
							boolean flag = false;
							if(!serSessions.isEmpty())
							{
							  for (SerSession serSession : serSessions) {
							    	if (serSession.nameString.equals(qstStrings[2])) {
									  
							    		for (Task task : tasks) {
											serSession.tasks.add(task);
										}
							    		
								    }
							  }
							}
							
							if (flag) {
								this.wait();
								serSessions.remove(this);
							}
							else {
								this.wait();
							}
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if (qstStrings[1].equals("RE") && iflogin) {
						Map<String, String> headsMap = readHeaders(input);
						
						int count = 0;
						
						if(headsMap.get("Code").equals("1"))////////////
						{
							StringBuffer bodyBuffer = new StringBuffer();
							StringBuffer reply = new StringBuffer("CS1.0 "+"Tasks"  +"\r\n");
							
							
							for (SerSession serSession : serSessions) {
								for (Task task : serSession.tasks) {
									
									bodyBuffer.append(task.nameString + "\r\n");
									bodyBuffer.append(task.styleString + "\r\n");
									bodyBuffer.append(task.phoneString + "\r\n");
									bodyBuffer.append(task.moneyString + "\r\n");
									bodyBuffer.append(task.deadlineString + "\r\n");
									bodyBuffer.append(task.discriptionString + "\r\n\r\n");
									
									count++;
								}
							}
							
							reply.append("Num "+count + "\r\n");
							reply.append("Code 1 \r\n");
						    reply.append("Content-Length "+bodyBuffer.length() +"\r\n\r\n");
							reply.append(bodyBuffer);
							output.write(reply.toString().getBytes());						
							
						}else if (headsMap.get("Code").equals("2")) {////////////////////
							StringBuffer bodyBuffer = new StringBuffer();
							StringBuffer reply = new StringBuffer("CS1.0 "+"Tasks"  +"\r\n");
							
							
							for (SerSession serSession : serSessions) {
								if (serSession.nameString.equals(nameString)) {
									for (Task task : serSession.tasks) {

										bodyBuffer.append(task.nameString + "\r\n");
										bodyBuffer.append(task.styleString + "\r\n");
										bodyBuffer.append(task.phoneString + "\r\n");
										bodyBuffer.append(task.moneyString + "\r\n");
										bodyBuffer.append(task.deadlineString + "\r\n");
										bodyBuffer.append(task.discriptionString + "\r\n\r\n");
										
										count++;
									}
								}
								
							}
							
							for (Task task : tasks) {

								bodyBuffer.append(task.nameString + "\r\n");
								bodyBuffer.append(task.styleString + "\r\n");
								bodyBuffer.append(task.phoneString + "\r\n");
								bodyBuffer.append(task.moneyString + "\r\n");
								bodyBuffer.append(task.deadlineString + "\r\n");
								bodyBuffer.append(task.discriptionString + "\r\n\r\n");
								
								count++;
							}
							
							reply.append("Num "+count + "\r\n");
							reply.append("Code 2 \r\n");
						    reply.append("Content-Length "+bodyBuffer.length() +"\r\n\r\n");
							reply.append(bodyBuffer);
							output.write(reply.toString().getBytes());
						}else if (headsMap.get("Code").equals("3")) {////////////////
							StringBuffer bodyBuffer = new StringBuffer();
							StringBuffer reply = new StringBuffer("CS1.0 "+"Tasks"  +"\r\n");
							
							
							for (SerSession serSession : serSessions) {
								for (Task task : serSession.tasks) {
									
									if (task.recipient.equals(nameString)) {
										bodyBuffer.append(task.nameString + "\r\n");
										bodyBuffer.append(task.styleString + "\r\n");
										bodyBuffer.append(task.phoneString + "\r\n");
										bodyBuffer.append(task.moneyString + "\r\n");
										bodyBuffer.append(task.deadlineString + "\r\n");
										bodyBuffer.append(task.discriptionString + "\r\n\r\n");
										
										count++;
									}									
								}
							}
							
							reply.append("Num "+count + "\r\n");
							reply.append("Code 3 \r\n");
						    reply.append("Content-Length "+bodyBuffer.length() +"\r\n\r\n");
							reply.append(bodyBuffer);
							output.write(reply.toString().getBytes());	
						}
						
					}else if (qstStrings[1].equals("PuTask") && iflogin) {
						Map<String, String> headsMap = readHeaders(input);
						
						boolean f = true;
						for (SerSession serSession : serSessions) {
							for (Task task : serSession.tasks) {
								if(task.nameString.equals(headsMap.get("TName"))){
									f = false;
								}
							}
						}
						
						if (f) {
							Task task = new Task();
							task.nameString = headsMap.get("TName");
							task.phoneString = headsMap.get("Phone");
							task.styleString = headsMap.get("Style");
							task.moneyString = headsMap.get("Money");
							task.deadlineString = headsMap.get("Deadline");
							
							int cl = Integer.parseInt(headsMap.get("Content-Length"));
							
							task.discriptionString = readResponseBody(input,cl).toString();
							
							tasks.add(task);
							respondMsg('1');
						}
						else{
							respondMsg('2');
						}
					}else if (qstStrings[1].equals("Ask") && iflogin) {
						Map<String, String> headsMap = readHeaders(input);
						
						String name = headsMap.get("TName");
						boolean f = false;
						for (SerSession serSession : serSessions) {
							for (Task task : serSession.tasks) {
								if(task.nameString.equals(name) && task.recipient.equals("")){
									f = true;
								}
							}
						}
						
						if (f) {
							respondMsg('3');
						}
						else {
							respondMsg('4');
						}
					}
		        }		           
			}finally {
		              sock.close();
		           }
		       } catch (IOException ex) {
		              
		       }			
		}
		
		private byte[] readResponseBody(InputStream in, int contentLength) throws IOException {  
	          
	        ByteArrayOutputStream buff = new ByteArrayOutputStream(contentLength);  
	          
	        int b;  
	        int count = 0;  
	        while(count++ < contentLength) {  
	            b = in.read();  
	            buff.write(b);  
	        }  
	          
	        return buff.toByteArray();  
	    }  

		private String readLine(InputStream in) throws IOException {  
	        int b;  
	          
	        ByteArrayOutputStream buff = new ByteArrayOutputStream();  
	        while((b = in.read()) != '\r') {  
	            buff.write(b);  
	        }  
	          
	        in.read();      // 读取 LF  
	          
	        String line = buff.toString();  
	          
	        return line;  
	    }
		
		private Map<String, String> readHeaders(InputStream in) throws IOException {  
	        Map<String, String> headers = new HashMap<String, String>();  
	          
	        String line;  
	          
	        while(!("".equals(line = readLine(in)))) {  
	            String[] nv = line.split(" ");     // 头部字段的名值都是以空格分隔的  
	            headers.put(nv[0], nv[1]);  
	        }  
	          
	        return headers;  
	    }
		
		private void respondMsg(char code) throws IOException{
			OutputStream output = sock.getOutputStream();
			
			 StringBuffer reply = new StringBuffer("CS1.0 "+"RESPOND\r\n");
			 reply.append("Code "+code+"\r\n");
			 reply.append("\r\n");
			 
			 output.write(reply.toString().getBytes());
		
	};
		

	}
	
	public class Task{
		public String nameString;
		public String styleString;
		public String phoneString;
		public String moneyString;
		public String deadlineString;
		public String discriptionString;
		public String recipient;//接受任务者
		public Task(){
			nameString = "";
			styleString = "";
			phoneString = "";
			moneyString = "";
			deadlineString = "";
			discriptionString = "";
			recipient = "";
		}
	}
}
