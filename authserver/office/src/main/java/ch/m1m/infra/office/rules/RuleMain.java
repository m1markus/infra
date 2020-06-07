package ch.m1m.infra.office.rules;

import org.apache.commons.jexl3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// https://commons.apache.org/proper/commons-jexl/reference/syntax.html

public class RuleMain {

    private static final Logger log = LoggerFactory.getLogger(RuleMain.class);

    public static void main(String... args) {

        RuleMain rule = new RuleMain();
        rule.execute();
        return;
    }

    private void execute() {
        log.info("running...");

        //evalSimpleBooleanExpr();
        evalScriptBoolean();
    }

    private void evalSimpleBooleanExpr() {
        final String func = "evalSimpleBooleanExpr";

        final JexlEngine jexl = new JexlBuilder().cache(512)
                .strict(false)
                .silent(false)
                .create();

        String textExpr = "true";
        JexlExpression e = jexl.createExpression(textExpr);

        // populate the context
        JexlContext context = new MapContext();
        //context.set("G1", 10);

        // work it out
        Object result = e.evaluate(context);
        log.info("{} result: {} ({})", func, result, result.getClass().getName());
    }

    private void evalScriptBoolean() {
        final String func = "evalScriptBoolean";

        final JexlEngine jexl = new JexlBuilder().cache(512)
                .strict(true)
                .silent(false)
                .create();

        String textExpr = "/* this is a comment */ "
                        + "log.info('SCRIPT running'); "
                        + "if (order.getInt('myNum1') == myNum1) { "
                        + "   return true; "
                        + "}               "
                        + "return false;   ";

        JexlScript script = jexl.createScript(textExpr);

        // populate the context
        JexlContext context = new MapContext();
        context.set("log", log);
        context.set("myNum1", 10);
        context.set("order", this);

        // work it out
        Object result = script.execute(context);
        log.info("{} result: {} ({})", func, result, result.getClass().getName());
    }

    public int getInt(String varName) {
        int rc = 99;
        if ("myNum1".equals(varName)) {
            rc = 10;
        }
        log.info("TRACE script getInt() called with varName={} return={}", varName, rc);
        return rc;
    }
}
