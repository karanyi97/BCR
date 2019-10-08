package Asymmetrik.BCR;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.CoreAnnotations.EntityTypeAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.MentionsAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class BusinessCardParser {
	
	public static ContactInfo getContactInfo(String text) {
     	String name = "";
     	String email = "";
     	String number = "";
        
        Properties props = new Properties();
        props.put("regexner.mapping", "tokens.rules");
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        
        List<CoreMap> mentions = document.get(MentionsAnnotation.class);
        List<String> potentialPhoneNumbers = new ArrayList<String>();
        
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        
        Pattern pattern = Pattern.compile("(\\d{10})|(([\\(]?([0-9]{3})[\\)]?)?[ \\.\\-]?([0-9]{3})[ \\.\\-]([0-9]{4}))");
        
        for (CoreMap sentence : sentences) {
        	String sen = sentence.get(TextAnnotation.class);
        	Matcher m = pattern.matcher(sen);
            if (m.find()) {
            	potentialPhoneNumbers.add(sen);
            }
        }
        
        for (CoreMap mention : mentions) {
        	String type = mention.get(EntityTypeAnnotation.class);
        	
        	if (type.equals("PERSON")) {
        		name = mention.toString();
        	} else if (type.equals("EMAIL")) {
        		email = mention.toString();
        	}
        }
        
        if (potentialPhoneNumbers.size() > 1) {
        	for (String num : potentialPhoneNumbers) {
        		if (!num.contains("Fax")) {
        			number = num;
        		}
        	}
        } else {
        	number = potentialPhoneNumbers.get(0);
        }
        
        number = number.replaceAll("\\D+","");
        
        ContactInfo info = new ContactInfo(name, number, email);
        return info;
	}

}
