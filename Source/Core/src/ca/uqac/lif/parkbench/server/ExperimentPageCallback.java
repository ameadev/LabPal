package ca.uqac.lif.parkbench.server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.parkbench.Experiment;
import ca.uqac.lif.parkbench.LabAssistant;
import ca.uqac.lif.parkbench.Laboratory;
import ca.uqac.lif.parkbench.ParkbenchTui;

public class ExperimentPageCallback extends TemplatePageCallback
{
	protected static final SimpleDateFormat s_dateFormat = new SimpleDateFormat();
	
	public ExperimentPageCallback(Laboratory lab, LabAssistant assistant)
	{
		super("/experiment", lab, assistant);
	}
	
	@Override
	public String fill(String page, Map<String,String> params)
	{
		if (!params.containsKey("id"))
			return "";
		int experiment_nb = Integer.parseInt(params.get("id"));
		Experiment e = m_lab.getExperiment(experiment_nb);
		if (e == null)
		{
			return "";
		}
		String out = page.replaceAll("\\{%TITLE%\\}", "Experiment #" + experiment_nb);
		out = out.replaceAll("\\{%EXP_NB%\\}", Integer.toString(experiment_nb));
		out = out.replaceAll("\\{%EXP_START%\\}", formatDate(e.getStartTime()));
		out = out.replaceAll("\\{%EXP_END%\\}", formatDate(e.getEndTime()));
		out = out.replaceAll("\\{%EXP_STATUS%\\}", ExperimentsPageCallback.getStatusLabel(m_assistant, e));
		out = out.replaceAll("\\{%EXP_ESTIMATE%\\}", ParkbenchTui.formatEta(e.getDurationEstimate(Laboratory.s_parkMips)));
		if (e.getEndTime() > 0)
		{
			out = out.replaceAll("\\{%EXP_DURATION%\\}", Long.toString(e.getEndTime() - e.getStartTime()));
		}
		out = out.replaceAll("\\{%EXP_BY%\\}", e.getWhoRan());
		out = out.replaceAll("\\{%EXP_DATA%\\}", renderHtml(e.getAllParameters()).toString());
		out = out.replaceAll("\\{%EXP_DESCRIPTION%\\}", e.getDescription());
		String error_msg = e.getErrorMessage();
		if (!error_msg.isEmpty())
		{
			out = out.replaceAll("\\{%FAIL_MSG%\\}", "<h2>Error message</h2><pre>" + error_msg + "</pre>");
		}
		return out;
	}
	
	protected static String formatDate(long timestamp)
	{
		if (timestamp < 0)
		{
			return "";
		}
		return s_dateFormat.format(new Date(timestamp));
	}
	
	public static StringBuilder renderHtml(JsonElement e)
	{
		StringBuilder out = new StringBuilder();
		if (e instanceof JsonString)
		{
			out.append(((JsonString) e).stringValue());
		}
		else if (e instanceof JsonNumber)
		{
			out.append(((JsonNumber) e).numberValue());
		}
		else if (e instanceof JsonMap)
		{
			JsonMap m = (JsonMap) e;
			out.append("<table class=\"json-table\">\n");
			for (String k : m.keySet())
			{
				out.append("<tr><th>").append(k).append("</th>");
				out.append("<td>");
				JsonElement v = m.get(k);
				out.append(renderHtml(v));
				out.append("</td></tr>\n");
			}
			out.append("</table>\n");
		}
		return out;
	}
	
	public String getExperimentList()
	{
		return "";
	}
}