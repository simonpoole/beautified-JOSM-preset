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

/**
 * Make a (semi-)nice HTML page out of the presets
 * 
 * Licence Apache 2.0
 * 
 * @author Simon Poole
 *
 */

public class Preset2Html {

	HashMap<String,MultiHashMap<String,String>>msgs = new HashMap<String,MultiHashMap<String,String>>();
	
	String inputFilename;
	
	void parseXML(final InputStream input, final PrintWriter pw)
			throws ParserConfigurationException, SAXException, IOException {
		SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
		pw.write("<?xml version='1.0' encoding='utf-8' ?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		pw.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		pw.write("<head>");
		pw.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>");
		pw.write("<link rel=\"stylesheet\" href=\"preset.css\" type=\"text/css\" />");
		pw.write("</head><body>");
		
		
        saxParser.parse(input, new HandlerBase() {
        	
        	Locator locator = null;
        	String group = null;
        	String preset = null;
        	String icon = null;
        	String icon2 = null;
        	String keys = null;
        	boolean optional = false;
        	
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
            		pw.write("<div class=\"group\"><h3>" + group + "</h3>\n");
            	} else if ("item".equals(name)) {
            		preset = attr.getValue("name");
            		icon = attr.getValue("icon");
            		if (icon != null && !"".equals(icon)) {
            			icon2 = icon.replace("ICONPATH:", "icons/png/");
            			icon2 = icon2.replace("ICONTYPE", "png");
            		}
            	} else if ("chunk".equals(name)) {
            	} else if ("separator".equals(name)) {
            	} else if ("label".equals(name)) {
            	} else if ("optional".equals(name)) {
            		optional = true;
            	} else if ("key".equals(name) || "multiselect".equals(name) || "combo".equals(name) || "check".equals(name) || "text".equals(name)) {
            		if (!optional) {
            			String key = attr.getValue("key");
            			String value = attr.getValue("value");
            			// only fixed keys 
            			if (key != null && !"".equals(key)) {
            				if (keys == null) {
            					keys = key + "=" + (value!=null?value:"*");
            				} else {
            					keys = keys + "<br>" + key + "=" + (value!=null?value:"*");
            				}
            			}
            		} 
            	} else if ("link".equals(name)) {
            	} else if ("role".equals(name)) {
            	} else if ("reference".equals(name)) {
            	} else if ("list_entry".equals(name)) {
            	} else if ("preset_link".equals(name)) {
            	}
            }
            
            @Override
            public void endElement(String name) throws SAXException {
            	if ("group".equals(name)) {
            		group = null;
            		pw.write("</div>\n");
            	} else if ("optional".equals(name)) {
            		optional = false;
            	} else if ("item".equals(name)) {
            		if (preset != null) {
            			pw.write("<div class=\"container\">");
            			if (icon != null && !"".equals(icon)) {
            				if (!icon2.equals(icon)) {
            					pw.write("<div class=\"preset\"><img src=\""+icon2+"\"><br>"+preset+"</div>");
            				} else {
            					pw.write("<div class=\"preset\">"+preset+"</div>");
            				}
            				if (keys != null) {
            					pw.write("<div class=\"popup\" \">" + keys + "</div>");
            				}
            				pw.write("</div>");
            			} else {
            				pw.write("<div class=\"preset\">"+preset+"</div>");
            				if (keys != null) {
            					pw.write("<div class=\"popup\" \">" + keys + "</div>");
            				}
            			}
           				pw.write("</div>");
            			preset = null;
            		}
        			keys = null;
            	} else if ("chunk".equals(name)) {
            		keys = null;
            	} else if ("combo".equals(name) || "multiselect".equals(name)) {
            	}
            }
            
            @Override
            public void endDocument() {
            	pw.write("</body>");
            	pw.flush();
            }
        });
	}
	

	
	private void setInputFilename(String fn) {
		inputFilename = fn;
	}
	
	public static void main(String[] args) {
		// defaults
		InputStream is = System.in;
		OutputStream os = System.out;
		Preset2Html p = new Preset2Html();
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
			p.parseXML(is, new PrintWriter(os));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
