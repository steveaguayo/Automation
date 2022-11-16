package kimble.automation.helpers.controlpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;

import kimble.automation.helpers.Cache;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.BaseTest;
import kimble.automation.Config;
import kimble.automation.KimbleOneTest;
import kimble.automation.KimbleOneTest.CleardownType;

public class ControlPanel {
	
	static {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public static <T, R> void runInBackground(Supplier<R> lambda) {
		(new SwingWorker(){

			@Override
			protected Object doInBackground() throws Exception {
				return lambda.get();
		}}).execute();
	}
	
	public static JCheckBox bindCheckBox(Object obj, String fieldName, Runnable callback) {
		return bindCheckBox(obj, fieldName, checkbox -> { callback.run(); });
	}
		
	public static JCheckBox bindCheckBox(Object obj, String fieldName, Consumer<JCheckBox> callback) {
		Class cls = obj.getClass();
		try {
			Field searchedField = null;
			for(Field f : cls.getFields())
				if(fieldName.equals(f.getName()))
					searchedField = f;
			if(searchedField == null)
				return null;
			JCheckBox checkBox = new JCheckBox();
			Field field = searchedField;
			ActionListener al = e -> {
				try {
					field.set(obj, checkBox.isSelected());
				} catch (Exception ee) { throw new RuntimeException("Failed to set the value: " + fieldName + " on an object of type: " + cls.getName(), ee); }
				if(callback != null)
					callback.accept(checkBox);
			};
			checkBox.addActionListener(al);
			checkBox.setSelected((boolean) field.get(obj));
			al.actionPerformed(null);
			return checkBox;
		} catch (Exception e) { throw new RuntimeException("Failed to bind the field: " + fieldName + " on an object of type: " + cls.getName(), e); }
	}
	
	static WindowListener windowExitListener = new WindowListener() {
		public void windowOpened(WindowEvent e) {}
		public void windowClosing(WindowEvent e) { 
			System.exit(0); 
		}
		public void windowIconified(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowActivated(WindowEvent e) {}
		public void windowDeactivated(WindowEvent e) {}
		public void windowClosed(WindowEvent e) {}
		
	};
	static List<KimbleOneTest> tests = new ArrayList();
	static JFrame frame;
	static Map<BaseTest, TestEditor> editors;
	
	static Lock lock = new ReentrantLock();
	
	// flags
	public static boolean isOpen = false;
	
	public static void open() {
		if(isOpen)
			return;
		isOpen = true;
		frame = new JFrame();
		frame.addWindowListener(windowExitListener);
		frame.setAlwaysOnTop(true);
		frame.setTitle("Automation tests control panel");
		frame.setMinimumSize(new Dimension(377, 144));
		frame.getContentPane().setLayout(new GridLayout(0, 1));
		frame.setLocation(25, 25);
		SwingUtilities.invokeLater(() -> {
			localRefresh();
			frame.setVisible(true);
		});
	}
	
	public static void notifyChange(KimbleOneTest test) {
		try {
			lock.lock();
			if(test.getStatus() != null && !tests.contains(test))
				tests.add(test);
		} finally { lock.unlock(); }
		refresh();
	}
	
	public static void refresh() {
		if(!isOpen)
			return;
		SwingUtilities.invokeLater(() -> {
			localRefresh();
		});
	}
	
	static void localRefresh() {
		try {
			lock.lock();
			if(editors == null)
				editors = new LinkedHashMap(); 
			for(KimbleOneTest t : tests) {
				if(!editors.containsKey(t)) {
					TestEditor editor = new TestEditor(t);
					editors.put(t, editor);
					frame.getContentPane().add(editor);
					frame.repaint();
				}
				editors.get(t).refresh();
				frame.setResizable(true);
				frame.pack();
				frame.setResizable(false);
			}
		} finally { lock.unlock(); }
	}
	
	public static class ConfigurationEditor extends JComponent {
		
		public Config config;
		
		public static final Dimension 
		labelSize = new Dimension(200, 20),
		fieldSize = new Dimension(200, 20),
		explanationSize = new Dimension(300, 100);
		
		JLabel 
		doWhat = new JLabel("<html><b>Configure your automation test run"),
		timestampLabel = new JLabel("Timestamp (only effects clean runs)"),
		dataCleardownLabel = new JLabel("Data cleardown action"),
		remoteExecutionLabel = new JLabel("Execute remotely"),
		validateResultsLabel = new JLabel("Validate expected results"),
		debugExecutionLabel = new JLabel("Debug execution"),
		packagedOrgLabel = new JLabel("Execute on packaged orgs"),
		remoteBrowserLabel = new JLabel("Remote browser"),
		remoteBrowserVersionLabel = new JLabel("Remote browser version"),
		abortOnFirstFailureLabel = new JLabel("Abort on first failure"),
		isRerunLabel = new JLabel("Is rerun"),
		enableRerunDialogLabel = new JLabel("Show dump chooser before each test?");
		
		JFormattedTextField timestampInput = new JFormattedTextField(Config.timeStampFormatter);
		
		JTextField remoteBrowserVersionInput = new JTextField();
		
		JComboBox<CleardownType> dataCleardownActionCombo = new JComboBox();
		JComboBox<String> remoteBrowserCombo = new JComboBox();
		
		JCheckBox 
		abortOnFirstFailureCheckbox,
		remoteExecutionCheckbox,
		validateResultsCheckbox,
		debugExecutionCheckbox,
		packagedOrgCheckbox,
		isRerunCheckbox,
		enableRerunDialogCheckbox;
		
		JComponent testSelector = new JPanel();
		JButton okButton = new JButton("Ok");
		
		JPanel leftPanel = new JPanel();
		JScrollPane rightPane = new JScrollPane(testSelector);
		
		GroupLayout layout;

		public ConfigurationEditor(Config config, Runnable callback) {
			config.overrideWith(Cache.getConfig());
			Cache.setConfig(config);
			
			Runnable saveCallback = () -> { Cache.setConfig(config); };
			
			// timestamp
			timestampInput.setText(config.dateTimeOverride);
			Color validColor = timestampInput.getForeground();
			Color invalidColor = Color.red;
			timestampInput.addCaretListener(e -> {
				try {
					if(timestampInput.isEditValid()) {
						timestampInput.setForeground(validColor);
						config.dateTimeOverride = timestampInput.getText();
						saveCallback.run();
					}
					else
						timestampInput.setForeground(invalidColor);
				} catch (Exception e1) { e1.printStackTrace(); }
			});
			
			// data cleardown action
			Arrays.asList(CleardownType.values()).forEach(type -> { dataCleardownActionCombo.addItem(type); });
			dataCleardownActionCombo.addActionListener(e -> {
				config.dataClearDownAction = (CleardownType) dataCleardownActionCombo.getSelectedItem();
				Cache.setConfig(config);
			});
			dataCleardownActionCombo.setSelectedItem(config.dataClearDownAction);

			// debug execution
			debugExecutionCheckbox = bindCheckBox(config, "debugExecution", saveCallback);
			
			// enable dump chooser
			enableRerunDialogCheckbox = bindCheckBox(config, "enableRerunDialog", saveCallback);
			
			// validate expected results
			validateResultsCheckbox = bindCheckBox(config, "validateExpectedResults", saveCallback);
			
			// abort on first failure
			abortOnFirstFailureCheckbox = bindCheckBox(config, "abortOnFirstValidationFailure", saveCallback);
			
			// is re-run 
			isRerunCheckbox = bindCheckBox(config, "isRerun", saveCallback);
			
			// run on packaged orgs
			packagedOrgCheckbox = bindCheckBox(config, "packagedOrg", checkbox -> {
				if(checkbox.isSelected())
					config.targetEnvironments = "environmentsPkg";
				else
					config.targetEnvironments = "environmentsDev";
				saveCallback.run();
			});
			
			// remote execution
			remoteExecutionCheckbox = bindCheckBox(config, "remoteExecution", checkbox -> {
				boolean selected = checkbox.isSelected();
				config.remoteExecution = selected;
				remoteBrowserLabel.setEnabled(selected);
				remoteBrowserCombo.setEnabled(selected);
				remoteBrowserVersionLabel.setEnabled(selected);
				remoteBrowserVersionInput.setEnabled(selected);
				saveCallback.run();
			});
			
			// remote browser combo
			remoteBrowserCombo.addItem("Chrome");
			remoteBrowserCombo.addItem("Firefox");
			remoteBrowserCombo.addActionListener(e -> {
				config.remoteBrowser = (String) remoteBrowserCombo.getSelectedItem();
				Cache.setConfig(config);
			});
			remoteBrowserCombo.setSelectedItem(config.remoteBrowser);

			// remote browser version
			remoteBrowserVersionInput.addCaretListener(e -> {
				config.remoteBrowserVersion = (String) remoteBrowserVersionInput.getText();
				Cache.setConfig(config);
			});
			remoteBrowserVersionInput.setText(config.remoteBrowserVersion);
			
			// ok button
			okButton.addActionListener(e -> {
				callback.run();
			});
			
			// test selector
			testSelector.setLayout(new GridLayout(config.testsToRun.size(), 1));
			config.testsToRun.forEach((name, isRun) -> {
				JCheckBox isRunCheckBox = new JCheckBox();
				isRunCheckBox.addActionListener(e -> {
					boolean newValue = !config.testsToRun.get(name);
					isRunCheckBox.setSelected(newValue);
					config.testsToRun.put(name, newValue);
					Cache.setConfig(config);
				});
				isRunCheckBox.setSelected(isRun);
				JPanel panel = new JPanel();
				JLabel label = new JLabel(name);
				panel.add(label);
				panel.add(isRunCheckBox);
				label.setPreferredSize(labelSize);
				label.setMaximumSize(labelSize);
				testSelector.add(panel);
			});
			
			leftPanel.setLayout(layout = new GroupLayout(leftPanel));
			
			layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGap(0, 35, 70)
					.addGroup(
						layout.createParallelGroup(Alignment.LEADING)
							.addGroup(
								layout.createSequentialGroup()
									.addGap(0, 35, 1000)
									.addComponent(doWhat)
									.addGap(0, 35, 1000))
							.addGroup(
								layout.createSequentialGroup()
									.addComponent(timestampLabel)
									.addGap(10)
									.addComponent(timestampInput))
							.addGroup(
								layout.createSequentialGroup()
									.addComponent(dataCleardownLabel)
									.addGap(10)
									.addComponent(dataCleardownActionCombo))
							.addGroup(
								layout.createSequentialGroup()
									.addComponent(abortOnFirstFailureLabel)
									.addGap(10)
									.addComponent(abortOnFirstFailureCheckbox))
							.addGroup(
								layout.createSequentialGroup()
									.addComponent(isRerunLabel)
									.addGap(10)
									.addComponent(isRerunCheckbox))
							.addGroup(
								layout.createSequentialGroup()
									.addComponent(validateResultsLabel)
									.addGap(10)
									.addComponent(validateResultsCheckbox))
							.addGroup(
									layout.createSequentialGroup()
										.addComponent(debugExecutionLabel)
										.addGap(10)
										.addComponent(debugExecutionCheckbox))
							.addGroup(
								layout.createSequentialGroup()
									.addComponent(enableRerunDialogLabel)
									.addGap(10)
									.addComponent(enableRerunDialogCheckbox))
							.addGroup(
								layout.createSequentialGroup()
									.addComponent(packagedOrgLabel)
									.addGap(10)
									.addComponent(packagedOrgCheckbox))
							.addGroup(
								layout.createSequentialGroup()
									.addComponent(remoteExecutionLabel)
									.addGap(10)
									.addComponent(remoteExecutionCheckbox))
							.addGroup(
								layout.createSequentialGroup()
									.addComponent(remoteBrowserLabel)
									.addGap(10)
									.addComponent(remoteBrowserCombo))
							.addGroup(
								layout.createSequentialGroup()
									.addComponent(remoteBrowserVersionLabel)
									.addGap(10)
									.addComponent(remoteBrowserVersionInput))
//							.addComponent(testSelector)
							.addGroup(
								layout.createSequentialGroup()
									.addGap(0, 35, 1000)
									.addComponent(okButton)
									.addGap(0, 35, 1000)))
					.addGap(0, 35, 70));
			layout.setVerticalGroup(
			   layout.createSequentialGroup()
					.addGap(35)
					.addComponent(doWhat)
					.addGap(35)
					.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(timestampLabel)
							.addComponent(timestampInput))
					.addGap(10)
					.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(dataCleardownLabel)
							.addComponent(dataCleardownActionCombo))
					.addGap(10)
					.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(abortOnFirstFailureLabel)
							.addComponent(abortOnFirstFailureCheckbox))
					.addGap(10)
					.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(isRerunLabel)
							.addComponent(isRerunCheckbox))
					.addGap(10)
					.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(validateResultsLabel)
							.addComponent(validateResultsCheckbox))
					.addGap(10)
					.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(debugExecutionLabel)
							.addComponent(debugExecutionCheckbox))
					.addGap(10)
					.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(enableRerunDialogLabel)
							.addComponent(enableRerunDialogCheckbox))
					.addGap(10)
					.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(packagedOrgLabel)
							.addComponent(packagedOrgCheckbox))
					.addGap(10)
					.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(remoteExecutionLabel)
							.addComponent(remoteExecutionCheckbox))
					.addGap(10)
					.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(remoteBrowserLabel)
							.addComponent(remoteBrowserCombo))
					.addGap(10)
					.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(remoteBrowserVersionLabel)
							.addComponent(remoteBrowserVersionInput))
					.addGap(10)
//					.addComponent(testSelector)
					.addGap(35)
					.addGroup(
						layout.createParallelGroup()
							.addComponent(okButton))
					.addGap(0, 35, 1000));
			
//			this.setPreferredSize(size);
			
			doWhat.setPreferredSize(explanationSize);
			
			timestampLabel.setPreferredSize(labelSize);
			timestampLabel.setMaximumSize(labelSize);
			timestampInput.setPreferredSize(fieldSize);
			timestampInput.setMaximumSize(fieldSize);
			
			dataCleardownLabel.setPreferredSize(labelSize);
			dataCleardownLabel.setMaximumSize(labelSize);
			dataCleardownActionCombo.setPreferredSize(fieldSize);
			dataCleardownActionCombo.setMaximumSize(fieldSize);
			
			abortOnFirstFailureLabel.setPreferredSize(labelSize);
			abortOnFirstFailureLabel.setMaximumSize(labelSize);
			abortOnFirstFailureCheckbox.setPreferredSize(fieldSize);
			abortOnFirstFailureCheckbox.setMaximumSize(fieldSize);
			
			isRerunLabel.setPreferredSize(labelSize);
			isRerunLabel.setMaximumSize(labelSize);
			isRerunCheckbox.setPreferredSize(fieldSize);
			isRerunCheckbox.setMaximumSize(fieldSize);
			
			remoteExecutionLabel.setPreferredSize(labelSize);
			remoteExecutionLabel.setMaximumSize(labelSize);
			remoteExecutionCheckbox.setPreferredSize(fieldSize);
			remoteExecutionCheckbox.setMaximumSize(fieldSize);
			
			validateResultsLabel.setPreferredSize(labelSize);
			validateResultsLabel.setMaximumSize(labelSize);
			validateResultsCheckbox.setPreferredSize(fieldSize);
			validateResultsCheckbox.setMaximumSize(fieldSize);
			
			debugExecutionLabel.setPreferredSize(labelSize);
			debugExecutionLabel.setMaximumSize(labelSize);
			debugExecutionCheckbox.setPreferredSize(fieldSize);
			debugExecutionCheckbox.setMaximumSize(fieldSize);
			
			enableRerunDialogLabel.setPreferredSize(labelSize);
			enableRerunDialogLabel.setMaximumSize(labelSize);
			enableRerunDialogCheckbox.setPreferredSize(fieldSize);
			enableRerunDialogCheckbox.setMaximumSize(fieldSize);
			
			packagedOrgLabel.setPreferredSize(labelSize);
			packagedOrgLabel.setMaximumSize(labelSize);
			packagedOrgCheckbox.setPreferredSize(fieldSize);
			packagedOrgCheckbox.setMaximumSize(fieldSize);
			
			remoteBrowserLabel.setPreferredSize(labelSize);
			remoteBrowserLabel.setMaximumSize(labelSize);
			remoteBrowserCombo.setPreferredSize(fieldSize);
			remoteBrowserCombo.setMaximumSize(fieldSize);
			
			remoteBrowserVersionLabel.setPreferredSize(labelSize);
			remoteBrowserVersionLabel.setMaximumSize(labelSize);
			remoteBrowserVersionInput.setPreferredSize(fieldSize);
			remoteBrowserVersionInput.setMaximumSize(fieldSize);
			
			rightPane.setPreferredSize(new Dimension(300, 500));
			rightPane.setMaximumSize(new Dimension(300, 500));

			this.setLayout(new BorderLayout());
			
			this.add(leftPanel, BorderLayout.WEST);
			this.add(rightPane, BorderLayout.CENTER);
		}
	}

	public static void editConfiguration(Config aConfig) {		
		CountDownLatch latch = new CountDownLatch(1);
		
		JDialog dialog = new JDialog();
		dialog.addWindowListener(windowExitListener);
		dialog.setAlwaysOnTop(true);
		ConfigurationEditor editor = new ConfigurationEditor(aConfig, () -> { dialog.dispose(); latch.countDown(); });
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		dialog.setTitle("Configure automation run");
		dialog.getContentPane().setLayout(new GridLayout(0, 1));
		dialog.getContentPane().add(editor);
		dialog.pack();
		dialog.setResizable(false);
		dialog.setLocation(25, 25);
		SwingUtilities.invokeLater(() -> {
			dialog.setVisible(true);
		});
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static class TestEditor extends JPanel {
		
		final KimbleOneTest test;
		
		JLabel nameLabel = new JLabel(), statusLabel = new JLabel(), pauseStageLabel = new JLabel();
		JComboBox pauseStage = new JComboBox<Stage>();
		JButton
		pauseButton = new JButton("Pause"),
		abortButton = new JButton("Abort"), 
		dumpStateButton = new JButton("Dump state");
		
		public TestEditor(KimbleOneTest t) {
			test = t;
			statusLabel.setForeground(Color.orange);
			pauseStageLabel.setText("Set the stage to pause at: ");
			pauseStage.setRenderer(new DefaultListCellRenderer() {
				public Component getListCellRendererComponent(JList list, Object stage, int index, boolean isSelected, boolean cellHasFocus) {
				    JLabel label = (JLabel) super.getListCellRendererComponent(list, stage, index, isSelected, cellHasFocus);
				    label.setText(((Stage) stage).getLogName());
				    label.setFont(pauseStage.getFont().deriveFont(4));
				    return label;
				}
			});	
			pauseStage.setPreferredSize(new Dimension(700, 25));
			pauseStage.addItem(new Stage(null, ""));
			test.getNormalisedStageList().forEach(stage -> { pauseStage.addItem(stage); });
			pauseStage.addActionListener(e -> { test.pauseStage = ((Stage) pauseStage.getSelectedItem()).getFullName(); });
			pauseButton.addActionListener(e -> {
				if(test.getStatus() == BaseTest.Status.running)
					test.setStatus(BaseTest.Status.paused);
				else if(test.getStatus() == BaseTest.Status.paused)
					test.setStatus(BaseTest.Status.running);
			});
			abortButton.setForeground(Color.red);
			abortButton.addActionListener(e -> {
				test.setStatus(BaseTest.Status.pendingAbort);
			});
			dumpStateButton.addActionListener(e -> {
				dumpStateButton.setEnabled(false);
				runInBackground(() -> {
					test.dumpState();
					SwingUtilities.invokeLater(() -> {
						dumpStateButton.setEnabled(true);
					});
					return null;
				});
			});
			this.add(nameLabel);
			this.add(statusLabel);
			this.add(pauseButton);
			this.add(pauseStageLabel);
			this.add(pauseStage);
			this.add(abortButton);
			this.add(dumpStateButton);
			refresh();
		}
		
		public void refresh() {
			if(test == null || test.state == null)
				return;
			nameLabel.setText(test.getTestNameLogFormat());
			statusLabel.setText(test.getStatus().name());
			boolean enabled = test.getStatus() == BaseTest.Status.running || test.getStatus() == BaseTest.Status.paused;
			abortButton.setEnabled(enabled);
			pauseStage.setEnabled(enabled);
			if(test.getStatus() == BaseTest.Status.running)
				pauseButton.setText("Pause");
			else if(test.getStatus() == BaseTest.Status.paused)
				pauseButton.setText("Continue");
		}
		
	}
	
	public static class FetchDumps extends JComponent {
		
		public static final Dimension 
		size = new Dimension(400, 250),
		labelSize = new Dimension(70, 25),
		fieldSize = new Dimension(200, 25),
		explanationSize = new Dimension(300, 100);
		
		JLabel 
		doWhat = new JLabel("<html><b>If you want to fetch the latest test dumps from cloudbees, insert your cloudbees username and password and press 'fetch'. Otherwise press 'don't fetch'"),
		unLabel = new JLabel("<html><b>username :"),
		pwLabel = new JLabel("<html><b>password :");
		
		JTextField
		pwField = new JTextField(),
		unField = new JTextField();
		
		JButton
		fetch = new JButton("fetch"),
		dontFetch = new JButton("don't fetch");
		
		URL cloudbeesUrl;
		GroupLayout layout;
		
		public FetchDumps(String url, ActionListener closeAction) {
			try {
				cloudbeesUrl = new URL(url);
			} catch (MalformedURLException e) { throw new RuntimeException("failed to create cloudbees url", e); }

			this.setLayout(layout = new GroupLayout(this));
			
			layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGap(0, 35, 70)
					.addGroup(
						layout.createParallelGroup(Alignment.CENTER)
							.addGroup(
								layout.createSequentialGroup()
									.addGap(0, 35, 1000)
									.addComponent(doWhat)
									.addGap(0, 35, 1000))
							.addGroup(
								layout.createSequentialGroup()
									.addGap(0, 35, 1000)
									.addComponent(unLabel)
									.addGap(10)
									.addComponent(unField)
									.addGap(0, 35, 1000))
							.addGroup(
								layout.createSequentialGroup()
									.addGap(0, 35, 1000)
									.addComponent(pwLabel)
									.addGap(10)
									.addComponent(pwField)
									.addGap(0, 35, 1000))
							.addGroup(
								layout.createSequentialGroup()
									.addGap(0, 35, 1000)
									.addComponent(fetch)
									.addGap(10)
									.addComponent(dontFetch)
									.addGap(0, 35, 1000)))
					.addGap(0, 35, 70));
			layout.setVerticalGroup(
			   layout.createSequentialGroup()
					.addGap(35)
					.addComponent(doWhat)
					.addGap(35)
					.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(unLabel)
							.addComponent(unField))
					.addGap(10)
					.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(pwLabel)
							.addComponent(pwField))
					.addGap(35)
					.addGroup(
						layout.createParallelGroup()
							.addComponent(fetch)
							.addComponent(dontFetch))
					.addGap(0, 35, 1000));
			
			this.setPreferredSize(size);
			
			doWhat.setPreferredSize(explanationSize);
			
			unLabel.setPreferredSize(labelSize);
			unLabel.setMaximumSize(labelSize);
			unField.setPreferredSize(fieldSize);
			unField.setMaximumSize(fieldSize);
			
			pwLabel.setPreferredSize(labelSize);
			pwLabel.setMaximumSize(labelSize);
			pwField.setPreferredSize(fieldSize);
			pwField.setMaximumSize(fieldSize);
			
			String username = (String) Cache.get("cbUsername");
			String password = (String) Cache.get("cbPassword");
			
			unField.setText(username != null? username : "");
			pwField.setText(password != null? password : "");
			
			fetch.addActionListener(e -> {
				String user = unField.getText(); //"benjamin.vellacott@kimbleapps.com";
				String pass = pwField.getText(); //"h1ndsite";
				doWhat.setText("Fetching the dumps from cloudbees...");
				unLabel.setVisible(false);
				unField.setVisible(false);
				pwLabel.setVisible(false);
				pwField.setVisible(false);
				fetch.setVisible(false);
				dontFetch.setVisible(false);
				
				runInBackground(() -> {
					fetchDumps(user, pass, cloudbeesUrl, 
					(successMsg) -> {
						doWhat.setText("<html>" + successMsg); 
						dontFetch.setText("Done");;
						dontFetch.setVisible(true);
					}, 
					(errorMsg) -> {
						doWhat.setText("<html>" + errorMsg);
						dontFetch.setText("Done");;
						dontFetch.setVisible(true);
					});
					return null;
				});
			});

			dontFetch.addActionListener(closeAction);
		}
	}
	
	public static void fetchDumps(String username, String password, URL cloudbeesUrl, Consumer<String> onSuccess, Consumer<String> onFailure) {
		Cache.set("cbUsername", username);
		Cache.set("cbPassword", password);
		try {
			String authStr = username + ":" +  password;
			String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));
		      
			URLConnection conn = cloudbeesUrl.openConnection();
			conn.setRequestProperty("Authorization", "Basic " + encoding);
			
	        ZipInputStream zipIn = new ZipInputStream(conn.getInputStream());
	        try {
	        	int fileCount = 0;
		        ZipEntry entry = zipIn.getNextEntry();
		        while (entry != null) {
		        	fileCount++;
		            String filePath = "./" + entry.getName();
		            if (!entry.isDirectory()) {
		            	File f = new File(filePath);
		            	f.getParentFile().mkdirs();
		            	OutputStream out = new FileOutputStream(f);
		            	try {
			            	IOUtils.copy(zipIn, out);
		            	} finally {
		            		out.flush();
		                	out.close();
		            	}
		            }
		            else
		                new File(filePath).mkdir();
		            zipIn.closeEntry();
		            entry = zipIn.getNextEntry();
		        }
		        int finalCount = fileCount;
				SwingUtilities.invokeLater(() -> { 
					onSuccess.accept("Successfully downloaded and extracted " + finalCount + " dump files");
				});
	        } catch(Exception ee) {
				String msg = "Failed to fetch the remote dump files - see the stacktrace in the console or command line - the dump files might not have been created yet";
				SwingUtilities.invokeLater(() -> { 
					onFailure.accept(msg);
				});
				new RuntimeException(msg, ee).printStackTrace();;
			} finally {
	            zipIn.close();
	        }
		} catch(Exception ee) {
			String msg = "Failed to fetch the remote dump files - see the stacktrace in the console or command line - the dump files might not have been created yet";
			SwingUtilities.invokeLater(() -> { 
				onFailure.accept(msg);
			});
			new RuntimeException(msg, ee).printStackTrace();;
		}	}
	
	public static void fetchDumpsUI(String dumpZipUrl) {
		CountDownLatch latch = new CountDownLatch(1);
		
		JDialog dialog = new JDialog();
		dialog.addWindowListener(windowExitListener);
		dialog.setAlwaysOnTop(true);
		JComponent editor = new FetchDumps(dumpZipUrl, e -> { dialog.dispose(); latch.countDown(); });
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		dialog.setTitle("Fetch from cloudbees");
		dialog.getContentPane().setLayout(new GridLayout(0, 1));
		dialog.getContentPane().add(editor);
		dialog.pack();
		dialog.setResizable(false);
		dialog.setLocation(25, 25);
		SwingUtilities.invokeLater(() -> {
			dialog.setVisible(true);
		});
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static class DumpChooser extends JComponent {
		
		public static final Dimension 
		size = new Dimension(400, 285),
		listSize = new Dimension(350, 100),
		explanationSize = new Dimension(350, 100);
		
		JLabel 
		doWhat = new JLabel("<html><b>Do you want to run a clean run or continue execution from a previous failure"),
		cleanRunLabel = new JLabel("Clean run"),
		listLabel = new JLabel("Pick a dump file created by a previously failed execution");
		
		JCheckBox cleanRun = new JCheckBox();
		JList<File> list = new JList();
		JButton execute = new JButton("execute");
		
		public DumpChooser(String testName, Consumer<String> onComplete) {
			File dumps = new File("./dumps");
			if(!dumps.exists())
				throw new RuntimeException("The directory: " + dumps.getAbsolutePath() + " doesn't exist");
			if(!dumps.isDirectory())
				throw new RuntimeException(dumps.getAbsolutePath() + " isn't a directory");
			
			ActionListener executeAction = e -> {
				if(getFile() == null)
					onComplete.accept("Executing a clean run");
				else
					onComplete.accept("Continuing a failed test run using the dump state file: " + getFile().getName());
			};

			File[] files = dumps.listFiles((dir, name) -> { return name.startsWith(testName); });
			SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy hhmmss");
			Arrays.sort(files, (f1, f2) -> { 
				Date date1, date2;
				try {
					String dateString = f1.getName().split("\\.")[0];
					dateString = dateString.substring(dateString.length() - 15);
					date1 = df.parse(dateString);
					dateString = f2.getName().split("\\.")[0];
					dateString = dateString.substring(dateString.length() - 15);
					date2 = df.parse(dateString);
					return date2.compareTo(date1); 
				} catch (Exception e) {
//					new RuntimeException("failed to sort the file list based on the date postfix in the filename", e).printStackTrace(); 
					return 0;
				}
			});
			list.setListData(files);
			list.setPreferredSize(listSize);
			list.setMinimumSize(listSize);
			list.setBorder(BorderFactory.createLineBorder(Color.black));
			list.addMouseListener(new MouseListener() {
				
				public void mouseReleased(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				
				public void mouseClicked(MouseEvent e) {
					if(list.isEnabled() && e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
						executeAction.actionPerformed(new ActionEvent(e.getSource(), e.getID(), "double clicked file"));
				}
			});
			
			cleanRun.addActionListener(e -> {
					listLabel.setEnabled(!cleanRun.isSelected());
					list.setEnabled(!cleanRun.isSelected());
				});
			cleanRun.setSelected(files.length == 0);
			cleanRun.setEnabled(files.length > 0);
			
			if(files.length > 0)
				list.setSelectedIndex(0);
			
			execute.addActionListener(executeAction);
			
			doWhat.setPreferredSize(explanationSize);
			this.setPreferredSize(size);
			
			layoutComponent();	
		}
		
		public File getFile() {
			return cleanRun.isSelected() ? null : list.getSelectedValue();
		}
		
		void layoutComponent() {
			GroupLayout layout = new GroupLayout(this);
			this.setLayout(layout);
			
			layout.setHorizontalGroup(
					layout.createSequentialGroup()
						.addGap(0, 35, 70)
						.addGroup(
							layout.createParallelGroup(Alignment.LEADING)
								.addGroup(
									layout.createSequentialGroup()
										.addGap(0, 35, 1000)
										.addComponent(doWhat)
										.addGap(0, 35, 1000))
								.addGroup(
									layout.createSequentialGroup()
										.addGap(30)
										.addComponent(cleanRunLabel)
										.addGap(10)
										.addComponent(cleanRun))
									.addGroup(
										layout.createSequentialGroup()
											.addGap(30)
											.addComponent(listLabel))
									.addGroup(
										layout.createSequentialGroup()
											.addGap(35)
											.addComponent(list)
											.addGap(35))
								.addGroup(
									layout.createSequentialGroup()
										.addGap(0, 35, 1000)
										.addComponent(execute)
										.addGap(0, 35, 1000)))
						.addGap(0, 35, 70));
				layout.setVerticalGroup(
				   layout.createSequentialGroup()
						.addGap(35)
						.addComponent(doWhat)
						.addGap(35)
						.addGroup(
							layout.createParallelGroup(Alignment.CENTER)
								.addComponent(cleanRunLabel)
								.addComponent(cleanRun))
						.addGap(10)
						.addComponent(listLabel)
						.addGap(5)
						.addComponent(list)
						.addGap(10)
						.addGroup(
							layout.createParallelGroup()
								.addComponent(execute))
						.addGap(10));
		}
	}
	
	public static File chooseDumpUI(String testName) {
		CountDownLatch latch = new CountDownLatch(1);
		
		JDialog dialog = new JDialog();
		dialog.addWindowListener(windowExitListener);
		dialog.setAlwaysOnTop(true);
		DumpChooser editor = new DumpChooser(testName, (msg) -> { System.out.println(msg); dialog.dispose(); latch.countDown(); });
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		dialog.setTitle(testName);
		dialog.getContentPane().setLayout(new GridLayout(0, 1));
		dialog.getContentPane().add(editor);
		dialog.pack();
		dialog.setResizable(false);
		dialog.setLocation(25, 25);
		SwingUtilities.invokeLater(() -> {
			dialog.setVisible(true);
		});
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return editor.getFile();
	}
	
	public static void main(String args[]) throws Exception {
		fetchDumpsUI("https://kimble.ci.cloudbees.com/job/Exec%20Kimble%201.25%20Automation%20Tests%20(Dev%20Org)%20-%20SauceLabs/ws/dumps/*zip*/dumps.zip");
		System.out.println(chooseDumpUI("Scenario112").getAbsolutePath());
	}
	
}
