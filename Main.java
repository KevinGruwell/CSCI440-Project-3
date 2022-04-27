import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
	
	private static Lexicon.Num chosen;
	private static boolean subChosen = false;

	public static void main(String[] args) {
		Lexicon lex = new Lexicon();
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter a sentence: ");
		String input = scan.nextLine();//"Alice gave Bob a picture of the boat.";
		input = input.substring(0, (input.indexOf(".") != -1) ? input.indexOf(".") : input.length());
		
		String[] split = input.split("\\s+");
		for(String s : split) {
			if(lex.getEntryFor(s) == null) {
				System.out.println("Invalid sentence. Word \'" + s + "\' not detected.");
				return;
			}
		}
		
		Queue<String> words = new LinkedList<String>();
		for(int i = 0; i < split.length; ++i)
			words.add(split[i]);
		
		String composite = "(S (MOOD declarative)\n";
		composite += getNounPhrase(words, lex);
		composite += getVerb(words, lex);
		
		String next_np = getNounPhrase(words, lex);
		if(!next_np.isEmpty()) {
			if(words.isEmpty()) {
				composite += next_np;
			}else {
				boolean wasSubj = false;
				if(next_np.indexOf("SUBJ") != -1) {
					next_np = next_np.replace("SUBJ", "IND-OBJ");
					wasSubj = true;
				}else next_np = next_np.replace("OBJ", "IND-OBJ");
				String[] splittemp = next_np.split("\\r?\\n|\\r");
				for(String s : splittemp) {
					composite += s + "\n";
					if(!s.equals(splittemp[splittemp.length - 1])) {
						composite += "   " + (wasSubj ? "" : " ");
					}
				}
				//composite += next_np.substring(0, next_np.indexOf("\n") + 1) + "    " + next_np.substring(next_np.indexOf("\n") + 1);
				composite += getNounPhrase(words, lex);
			}
		}
		
		System.out.println(padNewlines(composite, 3));
	}
	
	public static String getNounPhrase(Queue<String> words, Lexicon lex) {
		if(words.size() < 1) return "";
		
		Lexicon.Entry first = lex.getEntryFor(words.remove());
		if(first == null) return "";
		
		if(first.type == Lexicon.Type.ARTICLE) {
			if(words.size() < 1) return "";
			
			Lexicon.Entry second = lex.getEntryFor(words.remove());
			
			String full = "(OBJ (NP (DET %s)\n(NOUN %s)\n(NUM %s))";
			chosen = second.num[0];
			full = String.format(full, first.word, second.word, chosen.id);
			
			if(words.size() > 1) {
				Lexicon.Entry third = lex.getEntryFor(words.peek());
				if(third.type == Lexicon.Type.PREPOSITION) {
					words.remove();
					full += String.format("\n(MODS (PP (PREP %s)\n          ", third.word);
					full += padNewlines(getNounPhrase(words, lex), 10);
					full = full.substring(0, full.lastIndexOf("\n")) + "))";
				}
			}else full += ")))";
			
			return padNewlines(full, 9) + "\n";
		}else if(first.type == Lexicon.Type.PRONOUN || first.type == Lexicon.Type.NOUN || first.type == Lexicon.Type.NAME) {
			if(first.type == Lexicon.Type.PRONOUN) chosen = first.num[0];
			else if(first.type == Lexicon.Type.NOUN) chosen = first.num[0];
			else if(first.type == Lexicon.Type.NAME) chosen = Lexicon.Num.THIRD_SINGULAR;
			
			String obj_part = (subChosen) ? "OBJ" : "SUBJ";
			
			
			String nounpart = "(%s (NP (%s %s)\n    " + (subChosen ? "" : " ") + "     (NUM %s)))\n";
			subChosen = true;
			return String.format(nounpart, obj_part, first.type, first.word, chosen.id);
		}
		
		return "";
	}
	
	public static String getVerb(Queue<String> words, Lexicon lex) {
		if(words.size() < 1) return "";
		
		Lexicon.Entry first = lex.getEntryFor(words.remove());
		Lexicon.Entry second = lex.getEntryFor(words.peek());
		if(first == null) return "";
		
		if(second == null || (first.type == Lexicon.Type.VERB && second.type != Lexicon.Type.ADVERB)) {
			if(first.type == Lexicon.Type.VERB) {
				return padNewlines(String.format("(VERB (V (VERB %s)\n(TENSE %s)\n(NUM %s)))", first.root, first.tense, chosen.id), 9) + "\n";
			}else return "";
		}
		
		words.remove();
		
		Lexicon.Entry adv = (first.type == Lexicon.Type.ADVERB) ? first : second;
		Lexicon.Entry verb = (first.type == Lexicon.Type.VERB) ? first : second;
		
		String verbpart = "(VERB %s)\n(TENSE %s)\n(NUM %s)";
		String advpart = "(ADV %s)";
		
		String fin = "(VERB (V ";
		if(first.type == Lexicon.Type.ADVERB) {
			fin += String.format(advpart, adv.word) + padNewlines("\n", 9);
			fin += padNewlines(String.format(verbpart, verb.root, verb.tense, chosen.id) + "))", 9) + "\n";
		}else {
			fin += padNewlines(String.format(verbpart, verb.root, verb.tense, chosen.id) + "\n", 9);
			fin += String.format(advpart, adv.word) + "))\n";
		}
		
		return fin;
	}
	
	public static String padNewlines(String toPad, int spaces) {
		String torep = "\n" + " ".repeat(spaces);
		return toPad.replaceAll("\\\n", torep);
	}

}
