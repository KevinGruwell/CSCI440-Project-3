import java.util.ArrayList;
import java.util.List;

public class Lexicon {
	
	private List<Entry> entries;

	public Lexicon() {
		entries = new ArrayList<Entry>();
		
		// Wh-Pronouns
		entries.add(new Entry("what", Type.WH_PRONOUN, null, new Num[] {Num.THIRD_SINGULAR, Num.THIRD_PLURAL}, null));
		
		// Names
		entries.add(new Entry("Mary", Type.NAME));
		entries.add(new Entry("Alice", Type.NAME));
		entries.add(new Entry("Bob", Type.NAME));
		entries.add(new Entry("Josh", Type.NAME));
		
		// Verbs
		entries.add(new Entry("did", Type.VERB, Tense.PAST, new Num[] {Num.ALL}, new TypeSpecial[] {TypeSpecial.ALL}, "do"));
		entries.add(new Entry("walks", Type.VERB, Tense.PRESENT, new Num[] {Num.THIRD_SINGULAR}, "walk"));
		entries.add(new Entry("walked", Type.VERB, Tense.PAST, new Num[] {Num.ALL}, "walk"));
		entries.add(new Entry("painted", Type.VERB, Tense.PAST, new Num[] {Num.ALL}, "paint"));
		entries.add(new Entry("ran", Type.VERB, Tense.PAST, new Num[] {Num.ALL}, "ran"));
		entries.add(new Entry("showed", Type.VERB, Tense.PAST, new Num[] {Num.ALL}, "show"));
		entries.add(new Entry("give", Type.INFINITIVE_VERB, Tense.PAST, new Num[] {Num.FIRST_SINGUALR, Num.SECOND_SINGULAR, Num.THIRD_SINGULAR, Num.FIRST_PLURAL, Num.SECOND_PLURAL, Num.THIRD_PLURAL}, "give"));
		entries.add(new Entry("gave", Type.VERB, Tense.PAST, new Num[] {Num.ALL}, new TypeSpecial[] {TypeSpecial.ALL}, "give"));
		
		// Adverbs
		entries.add(new Entry("quickly", Type.ADVERB, Tense.ANY, new Num[] {Num.ALL}, null));
		
		// Pronouns
		entries.add(new Entry("me", Type.PRONOUN, null, new Num[] {Num.FIRST_SINGUALR}, null));
		
		// Articles
		entries.add(new Entry("a", Type.ARTICLE, null, new Num[] {Num.THIRD_SINGULAR}, null));
		entries.add(new Entry("the", Type.ARTICLE, null, new Num[] {Num.THIRD_SINGULAR, Num.THIRD_PLURAL}, null));
		entries.add(new Entry("her", Type.ARTICLE, null, new Num[] {Num.THIRD_SINGULAR, Num.THIRD_PLURAL}, "she"));
		entries.add(new Entry("his", Type.ARTICLE, null, new Num[] {Num.THIRD_SINGULAR, Num.THIRD_PLURAL}, "him"));
		
		// Nouns
		entries.add(new Entry("picture", Type.NOUN, null, new Num[] {Num.THIRD_SINGULAR}, "picture"));
		entries.add(new Entry("home", Type.NOUN, null, new Num[] {Num.THIRD_SINGULAR}, "home"));
		entries.add(new Entry("drawing", Type.NOUN, null, new Num[] {Num.THIRD_SINGULAR}, "drawing"));
		entries.add(new Entry("man", Type.NOUN, null, new Num[] {Num.THIRD_SINGULAR}, "man"));
		entries.add(new Entry("mom", Type.NOUN, null, new Num[] {Num.THIRD_SINGULAR}, "mom"));
		entries.add(new Entry("dreams", Type.NOUN, null, new Num[] {Num.THIRD_PLURAL}, "dream"));
		entries.add(new Entry("boat", Type.NOUN, null, new Num[] {Num.THIRD_SINGULAR}, "boat"));
		entries.add(new Entry("gift", Type.NOUN, null, new Num[] {Num.THIRD_SINGULAR}, "gift"));
		
		// Prepositions
		entries.add(new Entry("of", Type.PREPOSITION));
		entries.add(new Entry("to", Type.PREPOSITION));
		entries.add(new Entry("in", Type.PREPOSITION));
		entries.add(new Entry("and", Type.PREPOSITION));
		entries.add(new Entry("at", Type.PREPOSITION));
		entries.add(new Entry("with", Type.PREPOSITION));
	}
	
	public Entry getEntryFor(String word) {
		for(Entry e : this.entries) 
			if(e.word.equals(word)) return e;
			
		return null;
	}
	
	public enum Tense { PAST, PRESENT, FUTURE, ANY, INFINITIVE; }
	public enum Type { VERB, INFINITIVE_VERB, NOUN, NAME, ARTICLE, PRONOUN, WH_PRONOUN, PREPOSITION, ADVERB, ADJECTIVE; }
	public enum TypeSpecial { TRANSITIVE, BITRANSITIVE, ALL; }
	public enum Num {
		FIRST_SINGUALR("1s"), SECOND_SINGULAR("2s"), THIRD_SINGULAR("3s"), 
		FIRST_PLURAL("1p"), SECOND_PLURAL("2p"), THIRD_PLURAL("3p"), ALL("ALL"); 
		public final String id;
		
		Num(String id) { this.id = id; } 
	}

	public class Entry {
		public final String word;
		public final String root;
		public final Type type;
		public final Tense tense;
		public final Num[] num;
		public final TypeSpecial[] spectypes;
		
		private Entry(String word, Type type) { this(word, type, null, null, null, word); }
		private Entry(String word, Type type, Tense tense, Num[] num, String root) { this(word, type, tense, num, null, root); }
		private Entry(String word, Type type, Tense tense, Num[] num, TypeSpecial[] spectypes, String root) {
			this.word = (type == Type.NAME) ? word : word.toLowerCase();
			this.root = root;
			this.type = type;
			this.tense = tense;
			this.num = num;
			this.spectypes = spectypes;
		}
	}
	
}