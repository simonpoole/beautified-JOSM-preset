package ch.poole.presetutils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

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
	String vespucciLink = null;
	String josmLink = null;
	int groupCount = 0;
	
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
        	String chunk = null;
        	HashMap<String,String> chunkKeys = new HashMap<String,String>();
        	String icon = null;
        	String icon2 = null;
        	String keys = null;
        	boolean optional = false;
        	StringBuffer buffer = new StringBuffer();
        	
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
				if ("presets".equals(name)) {
					String shortdescription = attr.getValue("shortdescription");
					if (shortdescription == null) {
						pw.write("<h1>Presets from File " + inputFilename + "</h1>\n");
					} else {
					    pw.write("<h1>" + shortdescription + "</h1>\n");
					}
					if (vespucciLink != null) {
						try {
						    pw.write("<div class=\"download\"><a href=\"vespucci:/preset?preseturl=" + URLEncoder.encode(vespucciLink, "UTF-8") + "\">Download link for Vespucci</a><br>\n");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (josmLink != null) {
					    pw.write("<div class=\"download\"><a href=\"" + josmLink + "\">Download link for JOSM</a></div>\n");
					}
					pw.write("<p />");
				} else if ("group".equals(name)) {
					groupCount++;
            		group = attr.getValue("name");
            		buffer.append("<div class=\"group\"><h" + (groupCount + 1) + ">"); 
            		String groupIcon = attr.getValue("icon");
            		if (groupIcon != null && !"".equals(groupIcon)) {
            			String groupIcon2 = groupIcon.replace("ICONPATH:", "icons/png/");
            			groupIcon2 = groupIcon2.replace("ICONTYPE", "png");
            			if (!groupIcon.equals(groupIcon2)) {
            			    buffer.append("<img src=\""+groupIcon2+"\" style=\"vertical-align:middle\"> ");
            			}
            		}
            		buffer.append("<a name=\""+group + "\"></a>" + group + "</h" + (groupCount + 1) + ">\n");
            		if (groupCount == 1) {
            		    pw.write("<a href=\"#" + group + "\">" + group + "</a> ");
            		}
            	} else if ("item".equals(name)) {
            		preset = attr.getValue("name");
            		icon = attr.getValue("icon");
            		if (icon != null && !"".equals(icon)) {
            			icon2 = icon.replace("ICONPATH:", "icons/png/");
            			icon2 = icon2.replace("ICONTYPE", "png");
            		}
            	} else if ("chunk".equals(name)) {
            		chunk = attr.getValue("id");
            		keys="";
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
            		String ref = attr.getValue("ref");
            		String refKeys = chunkKeys.get(ref);
            		if (refKeys != null) {
            			keys = keys + refKeys;
            		} else {
            			System.err.println(ref + " was not found for preset " + preset);
            		}
            	} else if ("list_entry".equals(name)) {
            	} else if ("preset_link".equals(name)) {
            	}
            }
            
            @Override
            public void endElement(String name) throws SAXException {
            	if ("group".equals(name)) {
            		group = null;
            		buffer.append("</div>\n");
            		groupCount--;
            	} else if ("optional".equals(name)) {
            		optional = false;
            	} else if ("item".equals(name)) {
            		if (preset != null) {
            		    buffer.append("<div class=\"container\">");
            			if (icon != null && !"".equals(icon)) {
            				if (!icon2.equals(icon)) {
            				    buffer.append("<div class=\"preset\"><img src=\""+icon2+"\"><br>"+preset.replace("/", " / ")+"</div>");
            				} else {
            				    buffer.append("<div class=\"preset\">"+preset.replace("/", " / ")+"</div>");
            				}
            				if (keys != null) {
            				    buffer.append("<div class=\"popup\" \">" + keys + "</div>");
            				}
            				buffer.append("</div>");
            			} else {
            			    buffer.append("<div class=\"preset\">"+preset.replace("/", " / ")+"</div>");
            				if (keys != null) {
            				    buffer.append("<div class=\"popup\" \">" + keys + "</div>");
            				}
            			}
            			buffer.append("</div>");
            			preset = null;
            		}
        			keys = null;
            	} else if ("chunk".equals(name)) {
            		if (chunk != null) {
            			chunkKeys.put(chunk, keys);
            			// System.err.println("added chunk " + chunk);
            		} else {
            			System.err.println("chunk null");
            		}
            		keys = null;
            		chunk = null;
            	} else if ("combo".equals(name) || "multiselect".equals(name)) {
            	}
            }
            
            @Override
            public void endDocument() {
                pw.write(buffer.toString());
            	pw.write("<div class=\"footer\">Page generated by Preset2Html, Simon Poole</body>");
            	pw.flush();
            }
        });
	}
	
	private void setVespucciLink(String optionValue) {
		vespucciLink = optionValue;
	}
	
	private void setJosmLink(String optionValue) {
		josmLink = optionValue;
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
				.withDescription("input preset file, default: standard in")
				.create("input");

		Option outputFile = OptionBuilder.withArgName("file")
				.hasArg()
				.withDescription("output .pot file, default: standard out")
				.create("output");
		
		Option vespucciLink = OptionBuilder.withArgName("vespucci")
				.hasArg()
				.withDescription("download link vespucci format, default: none")
				.create("vespucci");
		
		Option josmLink = OptionBuilder.withArgName("josm")
				.hasArg()
				.withDescription("download link JOSM format, default: none")
				.create("josm");
		
		Options options = new Options();

		options.addOption(inputFile);
		options.addOption(outputFile);
		options.addOption(vespucciLink);
		options.addOption(josmLink);

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
			if (line.hasOption( "vespucci")) {
			    p.setVespucciLink(line.getOptionValue("vespucci"));
			}
			if (line.hasOption( "josm")) {
			    p.setJosmLink(line.getOptionValue("josm"));
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
