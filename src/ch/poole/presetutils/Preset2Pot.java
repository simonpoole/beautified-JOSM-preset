package ch.poole.presetutils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;



import java.util.Locale;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributeListImpl;

/**
 * Small utility to extract strings from a JOSM style preset file that should be translated, 
 * error handling is essentially crashing and burning when something goes wrong.
 * Some parts of this were nicked from Vespucci and some from Apache CLI sample code.
 * 
 * Licence Apache 2.0
 * 
 * @author Simon Poole
 *
 */

public class Preset2Pot {

	HashMap<String,MultiHashMap<String,String>>msgs = new HashMap<String,MultiHashMap<String,String>>();
	
	String inputFilename;
	MyHandler handler;
	
	
	public Locator getLocator() {
		return handler.locator;
	}
	
	class MyHandler extends HandlerBase {
    	
    	Locator locator = null;
    	String group = null;
    	String preset = null;
    	AttributeList mainAttr = null;
    	
    	String presetContext() {
    		return (group!=null?"|group:" + group.replace(' ', '_'):"") + (preset!=null?"|preset:" + preset.replace(' ', '_'):"");
    	}
    	
    	void addMsg(String tag, AttributeList attr, String keyName, String attrName, AttributeList mainAttr) {
    		String context = null;
    		if (mainAttr == null) {
    			context = attr.getValue("text_context");
    			if (context == null) {
    				context = attr.getValue("name_context");
    			}
    		} else {
    			// special case for list_entry
    			context = mainAttr.getValue("values_context");
    		}
    		if (!msgs.containsKey(context)) {
    			msgs.put(context,new MultiHashMap<String,String>());
    		}
    		String key = null;
    		if (keyName != null) {
    			key = attr.getValue(keyName);
    		}
    		String value = attr.getValue(attrName);
    		if (value != null && !"".equals(value)) {
    			msgs.get(context).add(value, inputFilename + ":" 
    					+ (locator !=null?locator.getLineNumber():0) 
    					+ "(" + tag + ":" + attrName + presetContext() + (key != null ? "|"+keyName+":"+key : "") + ")");
    		}
    	}
    	
    	void addValues(String keyName, String valueAttr, String tag, AttributeList attr, String defaultDelimiter) {
    		String displayValues = attr.getValue(valueAttr);
    		if (displayValues != null) {
    			String delimiter = attr.getValue("delimiter");
    			if (delimiter == null) {
    				delimiter = defaultDelimiter;
    			}
    			String context = attr.getValue("values_context");
    			
        		if (!msgs.containsKey(context)) {
        			msgs.put(context,new MultiHashMap<String,String>());
        		}
        		String key = null;
        		if (keyName != null) {
        			key = attr.getValue(keyName);
        		}
    			for (String s:displayValues.split(Pattern.quote(delimiter))) {
    				if (s != null && !"".equals(s)) {
            			msgs.get(context).add(s, inputFilename + ":" 
            					+ (locator !=null?locator.getLineNumber():0) 
            					+ "(" + tag + ":" + valueAttr + presetContext()  + (key != null ? "|"+keyName+":"+key : "") + ")");
            		}
    			}
    		}
    	}
    	
    	/** 
         * ${@inheritDoc}.
         */
		@Override
    	public void setDocumentLocator(Locator locator) {
			this.locator = locator;
		}

        /** 
         * ${@inheritDoc}.
         */
		@Override
        public void startElement(String name, AttributeList attr) throws SAXException {
        	if ("group".equals(name)) {
        		group = attr.getValue("name");
        		addMsg(name, attr, null, "name", null);
        	} else if ("item".equals(name)) {
        		preset = attr.getValue("name");
        		addMsg(name, attr, null, "name", null);
        		mainAttr = null;
        	} else if ("chunk".equals(name)) {
        		mainAttr = null;
        	} else if ("separator".equals(name)) {
        	} else if ("label".equals(name)) {
        		addMsg(name, attr, null, "text", null);
        	} else if ("optional".equals(name)) {
        		addMsg(name, attr, null, "text", null);
        	} else if ("key".equals(name)) {
        		addMsg(name, attr, "key", "text", null);
        		mainAttr = null;
        	} else if ("text".equals(name)) {
        		addMsg(name, attr, "key", "text", null);
        		mainAttr = null;
        	} else if ("link".equals(name)) {
        	} else if ("check".equals(name)) {
        		addMsg(name, attr, "key", "text", null);
        		mainAttr = null;
        	} else if ("combo".equals(name)) {
        		addMsg(name, attr, "key", "text", null);
        		String delimiter = attr.getValue("delimiter");
        		addValues("key","display_values", name, attr, delimiter != null ? delimiter : ",");
        		addValues("key","short_descriptions", name, attr, delimiter != null ? delimiter : ",");
        		mainAttr = new AttributeListImpl(attr);
        	} else if ("multiselect".equals(name)) {
        		addMsg(name, attr, "key", "text", null);
        		String delimiter = attr.getValue("delimiter");
        		addValues("key","display_values", name, attr, delimiter != null ? delimiter : ";");
        		addValues("key","short_descriptions", name, attr, delimiter != null ? delimiter : ";");
        		mainAttr = new AttributeListImpl(attr);
        	} else if ("role".equals(name)) {
        		addMsg(name, attr, "key", "text", null);
        	} else if ("reference".equals(name)) {
        	} else if ("list_entry".equals(name)) {
        		addMsg(name, attr, "value", "short_description", mainAttr);
        		addMsg(name, attr, "value", "display_value", mainAttr);
        	} else if ("preset_link".equals(name)) {
        	}
        }
        
        @Override
        public void endElement(String name) throws SAXException {
        	if ("group".equals(name)) {
        		group = null;
        	} else if ("optional".equals(name)) {
        	} else if ("item".equals(name)) {
        		preset = null;
        	} else if ("chunk".equals(name)) {
        	} else if ("combo".equals(name) || "multiselect".equals(name)) {
        	}
        }
	}
	
	
	void parseXML(InputStream input)
			throws ParserConfigurationException, SAXException, IOException {
		SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
		
		handler = new MyHandler();

        saxParser.parse(input, handler);
	}
	
	void dump2Pot(PrintWriter pw) {
		// print a header
		pw.print("msgid \"\"\n");
		pw.print("msgstr \"\"\n");
		pw.print("\"Project-Id-Version: PACKAGE VERSION\\n\"\n");
		// pw.print("\"POT-Creation-Date: 2015-11-02 23:02+0100\\n\"\n");
		String date = (new SimpleDateFormat("yyyy-MM-dd HH:mmZ", Locale.US)).format(new Date());
		pw.print("\"POT-Creation-Date: " + date + "\\n\"\n");
		pw.print("\"PO-Revision-Date: YEAR-MO-DA HO:MI+ZONE\\n\"\n");
		pw.print("\"Last-Translator: FULL NAME <EMAIL@ADDRESS>\\n\"\n");
		pw.print("\"Language-Team: LANGUAGE <LL@li.org>\\n\"\n");
		pw.print("\"MIME-Version: 1.0\\n\"\n");
		pw.print("\"Content-Type: text/plain; charset=UTF-8\\n\"\n");
		pw.print("\"Content-Transfer-Encoding: 8bit\\n\"\n");
		pw.print("\n");
		// dump the strings
		for (String context:msgs.keySet()) {
			MultiHashMap<String,String>map = msgs.get(context);
			for (String msgId:map.getKeys()) {
				// output locations
				pw.print("#:");
				for (String loc:map.get(msgId)) {
					pw.print(" " + loc);
				}
				pw.print("\n");
				if (context != null && !"".equals(context)) {
					pw.print("msgctxt \"" + context + "\"\n");
				}
				pw.print("msgid \"" + msgId + "\"\n");
				pw.print("msgstr \"\"\n");
				pw.print("\n");
			}
		}
		// trailer
		pw.print("#. Put one translator per line, in the form of NAME <EMAIL>, YEAR1, YEAR2\n");
		pw.print("#: " + inputFilename + ":0(None)\n");
		pw.print("msgid \"translator-credits\"\n");
		pw.print("msgstr \"\"\n");
		pw.print("\n");
		pw.flush();
	}
	
	private void setInputFilename(String fn) {
		inputFilename = fn;
	}
	
	public static void main(String[] args) {
		// defaults
		InputStream is = System.in;
		OutputStream os = System.out;
		Preset2Pot p = new Preset2Pot();
		p.setInputFilename("stdin");
		
		// arguments
		Option inputFile = OptionBuilder.withArgName("file")
				.hasArg()
				.withDescription(  "input preset file, default: standard in" )
				.create( "input" );

		Option outputFile = OptionBuilder.withArgName("file")
				.hasArg()
				.withDescription( "output .pot file, default: standard out" )
				.create( "output" );
		Options options = new Options();

		options.addOption(inputFile);
		options.addOption(outputFile);

		CommandLineParser parser = new DefaultParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse( options, args );
			if (line.hasOption( "input")) {
			    // initialise the member variable
			    String input = line.getOptionValue("input");
			    p.setInputFilename(input);
			    is = new FileInputStream(input);
			}
			if (line.hasOption( "output")) {
			    String output = line.getOptionValue("output");
			    os = new FileOutputStream(output);
			}
		}
		catch(ParseException exp) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "Preset2Pot", options );
			return;
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + e.getMessage());
			return;
		}
		
		try {
			p.setInputFilename("master_preset.xml");
			p.parseXML(is);
			p.dump2Pot(new PrintWriter(os));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			System.err.println("Error at line " + p.getLocator()!=null?p.getLocator().getLineNumber():"unknown line");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
