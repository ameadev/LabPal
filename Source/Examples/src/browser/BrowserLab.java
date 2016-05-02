package browser;

import ca.uqac.lif.parkbench.BarPlot;
import ca.uqac.lif.parkbench.CliParser.ArgumentMap;
import ca.uqac.lif.parkbench.Laboratory;

public class BrowserLab extends Laboratory
{
	public static void main(String[] args)
	{
		initialize(args, BrowserLab.class);
	}

	@Override
	public void setupExperiments(ArgumentMap map)
	{
		setTitle("Browser market share");
		BarPlot plot = new BarPlot();
		plot.useForX("browser", "Browser")
		.useForY("share", "Share").groupBy("market")
		.setTitle("My bar plot").assignTo(this);
		BarPlot stacked_plot = new BarPlot();
		stacked_plot.rowStacked().useForX("market", "Browser")
		.useForY("share", "Share").groupBy("browser")
		.setTitle("My bar plot").assignTo(this);

		add(new BrowserExperiment("IE", "video", 30), plot, stacked_plot);
		add(new BrowserExperiment("IE", "audio", 10), plot, stacked_plot);
		add(new BrowserExperiment("IE", "flash", 25), plot, stacked_plot);
		add(new BrowserExperiment("IE", "html", 20), plot, stacked_plot);
		add(new BrowserExperiment("IE", "js", 15), plot, stacked_plot);
		add(new BrowserExperiment("Firefox", "video", 20), plot, stacked_plot);
		add(new BrowserExperiment("Firefox", "audio", 5), plot, stacked_plot);
		add(new BrowserExperiment("Firefox", "flash", 35), plot, stacked_plot);
		add(new BrowserExperiment("Firefox", "html", 30), plot, stacked_plot);
		add(new BrowserExperiment("Firefox", "js", 10), plot, stacked_plot);		
	}

}