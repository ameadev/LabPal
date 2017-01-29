/*
  LabPal, a versatile environment for running experiments on a computer
  Copyright (C) 2014-2017 Sylvain Hallé

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.labpal.server;

import java.util.Calendar;

import ca.uqac.lif.jerrydog.CallbackResponse;
import ca.uqac.lif.jerrydog.Server;
import ca.uqac.lif.jerrydog.CallbackResponse.ContentType;
import ca.uqac.lif.labpal.FileHelper;
import ca.uqac.lif.labpal.LabAssistant;
import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.table.DataTable;
import ca.uqac.lif.labpal.table.Table;
import ca.uqac.lif.labpal.table.rendering.LatexTableRenderer;

import com.sun.net.httpserver.HttpExchange;

/**
 * Callback to download all tables as a single LaTeX file.
 * 
 * @author Sylvain Hallé
 *
 */
public class AllTablesCallback extends WebCallback
{
	public AllTablesCallback(Laboratory lab, LabAssistant assistant)
	{
		super("/all-tables", lab, assistant);
	}

	@Override
	public CallbackResponse process(HttpExchange t)
	{
		CallbackResponse response = new CallbackResponse(t);
		StringBuilder out = new StringBuilder();
		out.append("% ----------------------------------------------------------------").append(FileHelper.CRLF);
		out.append("% File generated by LabPal ").append(Laboratory.s_versionString).append(FileHelper.CRLF);
		out.append("% Date:     ").append(String.format("%1$te-%1$tm-%1$tY", Calendar.getInstance())).append(FileHelper.CRLF);
		out.append("% Lab name: ").append(m_lab.getTitle()).append(FileHelper.CRLF);
		out.append("%").append(FileHelper.CRLF).append("% To insert one of the tables into your text, do:").append(FileHelper.CRLF);
		out.append("% \\begin{table}").append(FileHelper.CRLF).append("% \\usebox{\\boxname}").append(FileHelper.CRLF).append("% \\end{table}").append(FileHelper.CRLF);
		out.append("% where \\boxname is one of the boxes defined in the file below").append(FileHelper.CRLF);
		out.append("% ----------------------------------------------------------------").append(FileHelper.CRLF).append(FileHelper.CRLF);
		for (int id : m_lab.getTableIds(true))
		{
			Table tab = m_lab.getTable(id);
			LatexTableRenderer renderer = new LatexTableRenderer(tab);
			String box_name = tab.getTitle();
			if (!tab.getNickname().isEmpty())
			{
				box_name = tab.getNickname();
			}
			if (box_name.compareTo("Untitled") == 0)
			{
				box_name += id;
			}
			box_name = LatexTableRenderer.formatName(box_name);
			DataTable d_tab = tab.getDataTable();
			String tab_contents = renderer.render(d_tab.getTree(), d_tab.getColumnNames());
			out.append("% ----------------------").append(FileHelper.CRLF).append("% Table: ").append(box_name).append(FileHelper.CRLF);
			out.append("% ----------------------").append(FileHelper.CRLF);
			out.append("\\newsavebox{\\").append(box_name).append("}").append(FileHelper.CRLF);
			out.append("\\begin{lrbox}{\\").append(box_name).append("}").append(FileHelper.CRLF);
			out.append(tab_contents);
			out.append(FileHelper.CRLF).append("\\end{lrbox}").append(FileHelper.CRLF).append(FileHelper.CRLF);
		}
		response.setContentType(ContentType.LATEX);
		String filename = Server.urlEncode(m_lab.getTitle()) + ".tex";
		response.setAttachment(filename);
		response.setContents(out.toString());
		return response;
	}
}
