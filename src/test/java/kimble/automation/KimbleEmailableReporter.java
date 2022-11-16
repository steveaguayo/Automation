package kimble.automation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;
import org.testng.reporters.EmailableReporter;

public class KimbleEmailableReporter extends EmailableReporter {

	protected PrintWriter createWriter(String outdir) throws IOException {
		new File(outdir).mkdirs();
		return new PrintWriter(new BufferedWriter(new FileWriter(new File(
				outdir, "kimble-emailable-report.html"))));
	}

	/** Starts HTML stream */
	protected void startHtml(PrintWriter out) {
		try {
			IOUtils.copy(new FileReader(
					"suite/templates/email-report-start.html"), out);
		} catch (Exception e) {
			throw new RuntimeException("failed to create an email report", e);
		}
	}

}
