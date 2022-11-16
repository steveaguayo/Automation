package kimble.automation.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import kimble.automation.domain.TestState;
import kimble.automation.helpers.ScenarioFunctions.Stage;
import kimble.automation.BaseTest;
import kimble.automation.BaseTest.Status;

import org.testng.SkipException;

public class ScenarioFunctions {
	
	public static class Stage implements Runnable {
		public String getFullName() { return generateStageName(this); }
		public String getLogName() { return generateStageLogName(this); }
		public List<String> getNameParts() { return getStageNames(this); }
		public Stage getRootStage() { return ScenarioFunctions.getRootStage(this); }
		public String getRootStageName() { return ScenarioFunctions.getRootStage(this).name; }
		public Stage parentStage;
		public String name;
		public List<Stage> childStages = new ArrayList();
		public boolean forceRun = false;
		public Runnable runnable;
		
		public Stage(Stage parentStage, String name) {
			if(parentStage != null) {
				this.parentStage = parentStage;
				this.parentStage.childStages.add(this);
			}
			this.name = name;
		}
	
		public Stage(Stage parentStage, String name, Consumer<Stage> consumer) {
			this(parentStage, name);
			runnable = () -> { consumer.accept(this); };
		}
		
		public Stage(Stage parentStage, String name, Runnable aRunnable) {
			this(parentStage, name);
			runnable = aRunnable;
		}
	
		
		public boolean isForceRun() {
			return this.forceRun || (this.parentStage != null && this.parentStage.isForceRun());
		}

		public void run(SeleniumHelper sh) {
			if(runnable != null)
				runnable.run();
		}

		public void run() {}
		
	}
	
//	/** Use this stage if you need the to switch into the classic iframe when executing in lightning */
//	public static class StageC extends Stage {
//		public StageC(Stage parentStage, String name, Consumer<Stage> consumer) { super(parentStage, name, consumer); }
//		public StageC(Stage parentStage, String name, Runnable aRunnable) { super(parentStage, name, aRunnable); }
//		public StageC(Stage parentStage, String name) { super(parentStage, name); }
//
//		public void run(SeleniumHelper sh) {
//			sh.switchToClassicIframe();
//			super.run(sh);
//		}
//	};
	
	/** Use this stage if you need the to switch out ot the classic iframe when executing in lightning */
	public static class StageZ extends Stage {
		public StageZ(Stage parentStage, String name, Consumer<Stage> consumer) { super(parentStage, name, consumer); }
		public StageZ(Stage parentStage, String name, Runnable aRunnable) { super(parentStage, name, aRunnable); }
		public StageZ(Stage parentStage, String name) { super(parentStage, name); }

		public void run(SeleniumHelper sh) {
			sh.switchOutOfClassicIframe();
			super.run(sh);
		}
	};
	
	/** See the javadoc for <b>normalizeAndExecute(BaseTest test, SeleniumHelper sh, Stage... stages) */
	public static List<Stage> normalise(Stage stage) {
		List<Stage> list = new ArrayList();
		list.add(stage);
		normalise(list, stage.childStages);
		return list;
	}
	
	/** See the javadoc for <b>normalizeAndExecute(BaseTest test, SeleniumHelper sh, Stage... stages) */
	public static List<Stage> normalise(Collection<Stage> stages) {
		List<Stage> list = new ArrayList();
		for(Stage stage : stages)
			normalise(list, stage);
		return list;
	}
	
	/** See the javadoc for <b>normalizeAndExecute(BaseTest test, SeleniumHelper sh, Stage... stages) */
	public static List<Stage> normalise(Stage... stages) {
		List<Stage> list = new ArrayList();
		for(Stage stage : stages)
			normalise(list, stage);
		return list;
	}
	
	/** See the javadoc for <b>normalizeAndExecute(BaseTest test, SeleniumHelper sh, Stage... stages) */
	public static void normalise(List<Stage> nameList, Stage stage) {
		nameList.add(stage);
		normalise(nameList, stage.childStages);
	}
	
	/** See the javadoc for <b>normalizeAndExecute(BaseTest test, SeleniumHelper sh, Stage... stages) */
	public static void normalise(List<Stage> nameList, Collection<Stage> stages) {
		for(Stage stage : stages)
			normalise(nameList, stage);
	}
	
	/** See the javadoc for <b>normalizeAndExecute(BaseTest test, SeleniumHelper sh, Stage... stages) */
	public static void normalise(List<Stage> nameList, Stage... stages) {
		for(Stage stage : stages)
			normalise(nameList, stage);
	}
	
	/** Naming convention is "parent stage name > child stage name" */
	public static String generateStageName(Stage stage) {
		StringBuilder sb = new StringBuilder();
		List<String>  names = getStageNames(stage);
		for(int i = 0; i < names.size();  i++)
			sb.append(names.get(i)).append(i < names.size()-1 ? " > " : "");
		return sb.toString();
	}
	
	/** Naming convention is "> stage name" */
	public static String generateStageLogName(Stage stage) {
		List<String> parts = stage.getNameParts();
		int level = parts.size();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < level; i++)
			sb.append("  ");
		sb.append("> ").append(parts.get(level-1));
		return sb.toString();
	}
	
	public static List<String> getStageNames(Stage stage) {
		List<String> names = new ArrayList();
		for(; stage != null; stage = stage.parentStage)
			names.add(0, stage.name);
		return names;
	}
	
	public static Stage getRootStage(Stage stage) {
		for(; stage.parentStage != null; stage = stage.parentStage);
		return stage;
	}
	
	/**
	 * Executes stages in their normalised form, meaning it will flatten the structure into a single sequence/list and
	 * then execute each stage one after the other i.e:
	 * 
	 * <pre>
	 * The stage structure
	 * <b>
	 * > stage A
	 *   > stage B
	 *     > stage C
	 *   > stage D
	 * </b>
	 * Is normalised into:
	 * <b>
	 * > stage A
	 * > stage B
	 * > stage C
	 * > stage D
	 * </b>
	 * And each stage is run one after the other
	 * </pre>
	 */
	public static void normaliseAndExecute(BaseTest test, SeleniumHelper sh, Stage... stages) {
		for(Stage stage : normalise(stages))
			execute(stage, test, sh);
	}

	/**
	 * Executes a test in as normalised form. Meaning it won't execute the child stages of the stage. For that use 
	 * <b>normalizeAndExecute(BaseTest test, SeleniumHelper sh, Stage... stages)
	 */
	public static void execute(Stage stage, BaseTest test, SeleniumHelper sh) {
		if(test.getStatus() == null)
			test.setStatus(BaseTest.Status.running);
		
		int tenthCounter = 0;
		if(stage.getFullName().equals(test.pauseStage))
			test.setStatus(Status.paused);
		while(test.getStatus() == Status.paused) {
			try { Thread.sleep(100); } catch (InterruptedException e) {}
			if(tenthCounter % 100 == 0)
				sh.LogMessageLine("!PAUSED AT STAGE: '> " + stage.name + "' FOR: " + tenthCounter / 10 + " SECONDS!");
			tenthCounter++; 
		}
		if(test.getStatus() == Status.pendingAbort) {
			test.state.stage = stage.getFullName();
			test.setStatus(Status.aborted);
			throw new SkipException("The test was aborted by the user and if rerun, using the date override, will continue execution from the stage: " + test.state.stage /*stage.getFullName()*/);
		}
		
		StringBuilder sb = new StringBuilder("|").append(test.getTestNameLoginFormat()).append("|  ").append(stage.getLogName());
		
		boolean pickupFromLastFailure = test.state.url != null && stage.getFullName().equals(test.state.stage);
		
		if(pickupFromLastFailure || stage.isForceRun()) {
			if(SeleniumHelper.config.debugExecution)
				System.out.println(sb);
			if(pickupFromLastFailure) {
				if(!test.state.url.equals(sh.getCurrentUrl()))
					sh.goToUrl(test.state.url);
				test.state.url = null;
				forceExecute(stage, test.state);
			}
			else
				stage.run();
		}
		else if(test.state.url == null) {
			if(SeleniumHelper.config.debugExecution)
				System.out.println(sb);
			forceExecute(stage, test.state);
		}
		else {
			sb.append(" !SKIPPED!");
			if(SeleniumHelper.config.debugExecution)
				System.out.println(sb);
		}
	}
	
	public static void forceExecute(Stage stage, TestState state) {
		state.stage = stage.getFullName();
		stage.run();
	}
}
