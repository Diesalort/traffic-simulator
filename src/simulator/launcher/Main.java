package simulator.launcher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	private static String _inFile = null;
	private static String _outFile = null;
	private static Factory<Event> _eventsFactory = null;
	private static Integer _timeLimit;
	private static String _mode;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTicksOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg()
				.desc("Ticks to the simulator's main loop (default value is 10).").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Select playing on gui or console").build());
		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null && !_mode.equals("gui")) {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {

		if (!_mode.equals("gui"))
			_outFile = line.getOptionValue("o");
	}

	private static void parseTicksOption(CommandLine line) {

		if (line.hasOption("t")) {
			_timeLimit = Integer.parseInt(line.getOptionValue("t")); // convertimos line.getOptionValue("t") (String) a
			// Integer
		} else {
			_timeLimit = 10;
		}
	}

	private static void parseModeOption(CommandLine line) throws ParseException {

		if (line.hasOption("m"))
			_mode = line.getOptionValue("m");
		else
			_mode = "gui";
		
		_mode = _mode.toLowerCase();
		
		if (!_mode.equals("gui") && !_mode.equals("console"))
			throw new ParseException("Invalid mode: " + _mode + ". Choose 'gui' or 'console'");
	}

	private static void initFactories() {

		// Factoría LightSwitchingStrategy
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add(new RoundRobinStrategyBuilder());
		lsbs.add(new MostCrowdedStrategyBuilder());
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);

		// Factoría DequeuingStrategy
		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add(new MoveFirstStrategyBuilder());
		dqbs.add(new MoveAllStrategyBuilder());
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);

		// Factoría de eventos
		List<Builder<Event>> ebs = new ArrayList<>();
		ebs.add(new NewJunctionEventBuilder(lssFactory, dqsFactory));
		ebs.add(new NewCityRoadEventBuilder());
		ebs.add(new NewInterCityRoadEventBuilder());
		ebs.add(new NewVehicleEventBuilder());
		ebs.add(new SetWeatherEventBuilder());
		ebs.add(new SetContClassEventBuilder());

		_eventsFactory = new BuilderBasedFactory<>(ebs);

	}

	private static void startBatchMode() throws IOException {

		// try with resources
		try (InputStream in = new BufferedInputStream(new FileInputStream(_inFile));
				OutputStream out = _outFile == null ? System.out
						: new BufferedOutputStream(new FileOutputStream(_outFile));) {

			TrafficSimulator _sim = new TrafficSimulator();
			Controller ctrl = new Controller(_sim, _eventsFactory);

			ctrl.loadEvents(in);
			ctrl.run(_timeLimit, out);
		}

	}

	private static void startGUIMode() throws IOException {

		TrafficSimulator _sim = new TrafficSimulator();
		Controller ctrl = new Controller(_sim, _eventsFactory);

		if (_inFile != null) {
			try (InputStream in = new BufferedInputStream(new FileInputStream(_inFile));) {

				ctrl.loadEvents(in);	
			}
		}

		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				new MainWindow(ctrl);
			}
		}); 

	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		
		if (_mode.equals("gui"))
			startGUIMode(); 
		else
			startBatchMode();
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
