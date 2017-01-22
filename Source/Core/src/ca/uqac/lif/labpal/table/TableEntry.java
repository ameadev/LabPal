/*
  LabPal, a versatile environment for running experiments on a computer
  Copyright (C) 2015-2017 Sylvain Hallé

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
package ca.uqac.lif.labpal.table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An entry in a data table
 */
public class TableEntry extends HashMap<String,Object>
{
	/**
	 * Dummy UID
	 */
	private static final transient long serialVersionUID = 1L;
	
	/**
	 * Associates to each key of the map to a set of datapoint IDs
	 */
	private final Map<String,Set<String>> m_datapointIds;
	
	public TableEntry()
	{
		super();
		m_datapointIds = new HashMap<String,Set<String>>();
	}
	
	public TableEntry(String key, Object value)
	{
		this(key, value, new String[]{});
	}
	
	public TableEntry(String key, Object value, String ... datapoint_ids)
	{
		this();
		put(key, value, datapoint_ids);
	}
	
	public TableEntry(TableEntry e)
	{
		this();
		putAll(e);
		m_datapointIds.putAll(e.m_datapointIds);
	}
	
	public void put(String key, Object value, String ... datapoint_ids)
	{
		put(key, value);
		Set<String> ids = new HashSet<String>();
		for (String id : datapoint_ids)
		{
			ids.add(id);
		}
		m_datapointIds.put(key, ids);
	}
	
	@Override
	public int hashCode()
	{
		int x = 0;
		for (Object o : values())
		{
			if (o != null)
			{
				x += o.hashCode();
			}
		}
		return x;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof TableEntry))
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}
		TableEntry te = (TableEntry) o;
		if (size() != te.size())
		{
			return false;
		}
		for (Map.Entry<String,Object> entry : entrySet())
		{
			Object e = te.get(entry.getKey());
			if (e == null)
			{
				if (entry.getValue() == null)
					continue; // Both are null
				else
					return false;
			}
			assert e != null;
			if (!e.equals(entry.getValue()))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gets the datapoint IDs associated to a key
	 * @param key
	 * @return
	 */
	public Set<String> getDatapointIds(String key)
	{
		if (!m_datapointIds.containsKey(key))
		{
			return new HashSet<String>();
		}
		return m_datapointIds.get(key);
	}
	
	/**
	 * Adds a new data point as a dependency of a datapoint contained in this
	 * table entry.
	 * @param key The key corresponding to the table entry
	 * @param dependency The data point entry to add as a dependency
	 */
	public void addDependency(String key, String dependency)
	{
		Set<String> deps = null;
		if (m_datapointIds.containsKey(key))
		{
			deps = m_datapointIds.get(key);
		}
		if (deps == null)
		{
			deps = new HashSet<String>();
		}
		deps.add(dependency);
		m_datapointIds.put(key, deps);
	}
	
	/**
	 * Adds a new data point as a dependency of a datapoint contained in this
	 * table entry.
	 * @param key The key corresponding to the table entry
	 * @param dependencies A set of data point entry to add as a dependency
	 */
	public void addDependency(String key, Set<String> dependencies)
	{
		Set<String> deps = null;
		if (m_datapointIds.containsKey(key))
		{
			deps = m_datapointIds.get(key);
		}
		if (deps == null)
		{
			deps = new HashSet<String>();
		}
		deps.addAll(dependencies);
		m_datapointIds.put(key, deps);
	}
}
