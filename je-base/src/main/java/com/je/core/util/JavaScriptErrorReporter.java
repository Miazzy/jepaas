package com.je.core.util;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

/**
 * 吝哥前端压缩JS需要   需确认  是否可删除
 */
public class JavaScriptErrorReporter implements ErrorReporter {

	@Override
	public void error(String arg0, String arg1, int arg2, String arg3, int arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public EvaluatorException runtimeError(String arg0, String arg1, int arg2,
			String arg3, int arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void warning(String arg0, String arg1, int arg2, String arg3,
			int arg4) {
		// TODO Auto-generated method stub

	}

}