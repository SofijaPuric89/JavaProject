/*
 [The "BSD licence"]
 Copyright (c) 2013 Tom Everett
 All rights reserved.
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.
 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

grammar bnf;

@header {
    import java.io.OutputStream;
    import java.io.PrintStream;
    import com.etf.rti.p1.compiler.AParser;
    import org.apache.commons.lang3.StringEscapeUtils;
    import java.util.LinkedList;
    import com.etf.rti.p1.translator.ebnf.elements.Nonterminal;
    import com.etf.rti.p1.translator.ebnf.elements.Terminal;
    import com.etf.rti.p1.translator.ebnf.rules.IRule;
    import com.etf.rti.p1.translator.ebnf.rules.SimpleRule;

}

options {
    superClass=AParser;
    }

@parser::members {
    private PrintStream output = null;
    private boolean first = true;
    private String name;
    private SimpleRule rule = new SimpleRule();
    private LinkedList<IRule> listRules = new LinkedList<IRule>();

    public bnfParser(TokenStream input, OutputStream out, String name) {
        this(input);
        this.output = new PrintStream(out);
        this.name = name;
    }

    public LinkedList<IRule> getRules() {
        return listRules;
    }

    private void print(String s) {
        if (output != null)
            output.print(s);
    }

    private void println(String s) {
        print(s);
        print("\n");
    }

    private void closeOutput() {
        if (output != null)
            output.close();
    }

    private void printFirst(String s) {
        if (first) {
            println(String.format("grammar %s;",name));
            println("options {\n" +
                "superClass=AParser;\n"+
                "}\n"+
            "@header {\n"+
                "import com.etf.rti.p1.compiler.AParser;\n" +
                "import com.etf.rti.p1.translator.ebnf.rules.IRule;\n" +
                "import java.util.LinkedList;\n" +
            "}\n" +
            "@parser::members {\n" +
                        "    public LinkedList<IRule> getRules() {\n" +
                        "        return null;\n" +
                        "    }}\n");
            println(String.format("init : %s ;", s));
        }
        first = false;
    }
}

@parser::implements {
    IParser
}

init
    : rulelist {closeOutput();}
;

rulelist
    : rule_* EOF
;

rule_
    : lhs ASSIGN rhs { printFirst($lhs.v);
                      println($lhs.v);
                      println($rhs.v);
                      println(";");
                      rule.setRule(new Nonterminal($lhs.v));
                      listRules.addLast(rule);
                      rule = new SimpleRule();
                      }
    ;

lhs returns [String v]
    : id {$v = $id.v;}
    ;

rhs returns [String v]
    : alternatives["\n\t|"] {$v = "\t: " + $alternatives.v;}
    ;

alternatives[String conector] returns [String v]
    : a=alternative{$v = $a.v; rule.setNewOptionForInsert();}
        (BAR b=alternative {$v += conector + $b.v; rule.setNewOptionForInsert();})*
    ;

alternative returns [String v]
    : (element { if ($v == null) $v = $element.v; else $v += " " + $element.v;}) *
    ;

element returns[String v]
    : optional {$v = $optional.v;}
    | zeroormore {$v = $zeroormore.v;}
    | oneormore {$v = $oneormore.v;}
    | t=text {$v = "\'" + StringEscapeUtils.escapeJava($t.text) + "\'";
                rule.insertElem(new Terminal($t.text));}
    | id {$v = $id.v;
          rule.insertElem(new Nonterminal($id.v));}
    ;

optional returns[String v]
    : REND alternatives["\n\t|"]{$v = $alternatives.v + "?";} LEND
    ;

zeroormore returns[String v]
    : RBRACE alternatives["\n\t|"]{$v = $alternatives.v + "*";} LBRACE
    ;

oneormore returns[String v]
    : RPAREN alternatives["\n\t|"]{$v = $alternatives.v + "+";} LPAREN
    ;

text
    : (TERMINAL | LETTER | ID)+
    ;

id returns [String v]
    : LT ruleid GT {$v = $ruleid.text;}
    ;

ruleid
    : LETTER (LETTER | ID)*
    ;

ASSIGN
    : '::='
    ;

LPAREN
    : ')'
    ;

RPAREN
    : '('
    ;

LBRACE
    : '}'
    ;

RBRACE
    : '{'
    ;

LEND
    : ']'
    ;

REND
    : '['
    ;

BAR
    : '|'
    ;

GT
    : '>'
    ;

LT
    : '<'
    ;

TERMINAL
    : ('"'|'\''|'\\'|'.'|':'|','|'?'|'!')
    ;

ID
    :  ('0'..'9'|'_')
    ;

LETTER
    : ('a'..'z'|'A'..'Z')
    ;

WS
    : (' '|'\t'|'\r'|'\n')+ -> skip
    ;