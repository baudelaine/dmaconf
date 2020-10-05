package com.dma.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Servlet implementation class AppendSelectionsServlet
 */
@WebServlet(name = "UpdateModel", urlPatterns = { "/UpdateModel" })
public class UpdateModelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateModelServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		Map<String, Object> result = new HashMap<String, Object>();

		try {
			
			result.put("CLIENT", request.getRemoteAddr() + ":" + request.getRemotePort());
			result.put("SERVER", request.getLocalAddr() + ":" + request.getLocalPort());
			
			result.put("FROM", this.getServletName());
			
			String user = request.getUserPrincipal().getName();
			result.put("USER", user);

			result.put("JSESSIONID", request.getSession().getId());
			
			Path wks = Paths.get(getServletContext().getRealPath("/datas") + "/" + user);			
			result.put("WKS", wks.toString());
			
			Path prj = Paths.get((String) request.getSession().getAttribute("projectPath"));
			result.put("PRJ", prj.toString());
			
			Map<String, Object> parms = Tools.fromJSON(request.getInputStream());

			
			if(parms != null) {

				
				@SuppressWarnings("unchecked")
				List<QuerySubject> model = (List<QuerySubject>) Tools.fromJSON((String) parms.get("model"), new TypeReference<List<QuerySubject>>(){});

				Map<String, Map<String, Object>> tMap = new HashMap<String, Map<String, Object>>();
				
				for(QuerySubject qs: model) {
					String table = qs.getTable_name();
					List<Field> fields = qs.getFields();
					Map<String, Object> fMap = new HashMap<String, Object>();
					for(Field field: fields) {
						String fieldName = field.getField_name();
						fMap.put(fieldName, null);
					}
					tMap.put(table, fMap);
				}

				boolean isXML = false;
				Project project = (Project) request.getSession().getAttribute("currentProject");
				Resource resource = project.getResource();
				if(resource.getJndiName().equalsIgnoreCase("XML")) {
					isXML = true;
				}				

				Map<String, List<Field>> newFields = new HashMap<String, List<Field>>();
				
				if(isXML) {
					@SuppressWarnings("unchecked")
					Map<String, QuerySubject> qss = (Map<String, QuerySubject>) request.getSession().getAttribute("QSFromXML");
					
					for(Entry<String, QuerySubject> qs: qss.entrySet()) {
						String table = qs.getKey();
						if(tMap.containsKey(table)){
							List<Field> fields = qs.getValue().getFields();
							for(Field field: fields) {
								if(!tMap.get(table).containsKey(field.getField_name())) {
	
									if(!newFields.containsKey(table)) {
										newFields.put(table, new ArrayList<Field>());
									}
									
									Field newField = new Field();
									newField.set_id(field.getField_name() + field.getField_type());
									newField.setField_name(field.getField_name());
									newField.setField_type(field.getField_type());
									
									newFields.get(table).add(newField);
								}
							}
						}
					}
				}
				
				Map<String, List<Field>> datas = new HashMap<String, List<Field>>();
				
				for(QuerySubject qs: model) {
					String table = qs.getTable_name();
					if(newFields.containsKey(table)) {
						datas.put(qs.get_id(), newFields.get(table));
						qs.getFields().addAll(newFields.get(table));
					}
				}
				
				result.put("MODEL", model);
				result.put("DATAS", datas);
				result.put("STATUS", "OK");
			}
			else {
				result.put("STATUS", "KO");
				result.put("ERROR", "Input parameters are not valid.");
				throw new Exception();
			}			
		}
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			result.put("STATUS", "KO");
			result.put("EXCEPTION", e.getClass().getName());
			result.put("MESSAGE", e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			result.put("STACKTRACE", sw.toString());
			e.printStackTrace(System.err);
		}

		finally{
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(Tools.toJSON(result));
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}