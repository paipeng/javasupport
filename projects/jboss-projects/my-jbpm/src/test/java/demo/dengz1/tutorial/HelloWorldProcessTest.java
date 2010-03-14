package demo.dengz1.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.junit.Before;
import org.junit.Test;

public class HelloWorldProcessTest {

	@Before
	public void setup() {
		// Each test will start with setting the static isExecuted
		// member of MyActionHandler to false.
		MyActionHandler.isExecuted = false;
	}

	@Test
	public void testTransitionAction2() {
		ProcessDefinition processDefinition = ProcessDefinition
				.parseXmlString("<process-definition>"
						+ " <start-state>"
						+ " <transition to='s' />"
						+ " </start-state>"
						+ " <state name='s'>"
						+ " <event type='node-enter'>"
						+ " <action class='demo.dengz1.tutorial.MyActionHandler' />"
						+ " </event>"
						+ " <event type='node-leave'>"
						+ " <action class='demo.dengz1.tutorial.MyActionHandler' />"
						+ " </event>" + " <transition to='end'/>" + " </state>"
						+ " <end-state name='end' />" + "</process-definition>");
		ProcessInstance processInstance = new ProcessInstance(processDefinition);
		assertFalse(MyActionHandler.isExecuted);
		// The next signal will cause the execution to leave the start
		// state and enter the state 's'. So the state 's' is entered
		// and hence the action is executed.
		processInstance.signal();
		assertTrue(MyActionHandler.isExecuted);
		// Let's reset the MyActionHandler.isExecuted
		MyActionHandler.isExecuted = false;
		// The next signal will trigger execution to leave the
		// state 's'. So the action will be executed again.
		processInstance.signal();
		// Voila.
		assertTrue(MyActionHandler.isExecuted);
	}

	@Test
	public void testTransitionAction() {
		// The next process is a variant of the hello world process.
		// We have added an action on the transition from state 's'
		// to the end-state. The purpose of this test is to show
		// how easy it is to integrate java code in a jBPM process.
		ProcessDefinition processDefinition = ProcessDefinition
				.parseXmlString("<process-definition>" + " <start-state>"
						+ " <transition to='s' />" + " </start-state>"
						+ " <state name='s'>" + " <transition to='end'>"
						+ " <action class='demo.dengz1.tutorial.MyActionHandler' />"
						+ " </transition>" + " </state>"
						+ " <end-state name='end' />" + "</process-definition>");
		// Let's start a new execution for the process definition.
		ProcessInstance processInstance = new ProcessInstance(processDefinition);
		// The next signal will cause the execution to leave the start
		// state and enter the state 's'
		processInstance.signal();
		// Here we show that MyActionHandler was not yet executed.
		assertFalse(MyActionHandler.isExecuted);
		// ... and that the main path of execution is positioned in
		// the state 's'
		assertSame(processDefinition.getNode("s"), processInstance
				.getRootToken().getNode());
		// The next signal will trigger the execution of the root
		// token. The token will take the transition with the
		// action and the action will be executed during the
		// call to the signal method.
		processInstance.signal();
		// Here we can see that MyActionHandler was executed during
		// the call to the signal method.
		assertTrue(MyActionHandler.isExecuted);
	}

	@Test
	public void testTaskAssignment() {
		// The process shown below is based on the hello world process.
		// The state node is replaced by a task-node. The task-node
		// is a node in JPDL that represents a wait state and generates
		// task(s) to be completed before the process can continue to
		// execute.
		ProcessDefinition processDefinition = ProcessDefinition
				.parseXmlString("<process-definition name='the baby process'>"
						+ " <start-state>"
						+ " <transition name='baby cries' to='t' />"
						+ " </start-state>"
						+ " <task-node name='t'>"
						+ " <task name='change nappy'>"
						+ " <assignment class='demo.dengz1.tutorial.NappyAssignmentHandler' />"
						+ " </task>" + " <transition to='end' />"
						+ " </task-node>" + " <end-state name='end' />"
						+ "</process-definition>");
		// Create an execution of the process definition.
		ProcessInstance processInstance = new ProcessInstance(processDefinition);
		Token token = processInstance.getRootToken();
		// Let's start the process execution, leaving the start-state
		// over its default transition.
		token.signal();
		// The signal method will block until the process execution
		// enters a wait state. In this case, that is the task-node.
		assertSame(processDefinition.getNode("t"), token.getNode());
		// When execution arrived in the task-node, a task 'change nappy'
		// was created and the NappyAssignmentHandler was called to determine
		// to whom the task should be assigned. The NappyAssignmentHandler
		// returned 'papa'.
		// In a real environment, the tasks would be fetched from the
		// database with the methods in the org.jbpm.db.TaskMgmtSession.
		// Since we don't want to include the persistence complexity in
		// this example, we just take the first task-instance of this
		// process instance (we know there is only one in this test
		// scenario).
		TaskInstance taskInstance = (TaskInstance) processInstance
				.getTaskMgmtInstance().getTaskInstances().iterator().next();
		// Now, we check if the taskInstance was actually assigned to 'papa'.
		assertEquals("papa", taskInstance.getActorId());
		// Now we suppose that 'papa' has done his duties and mark the task
		// as done.
		taskInstance.end();
		// Since this was the last (only) task to do, the completion of this
		// task triggered the continuation of the process instance execution.
		assertSame(processDefinition.getNode("end"), token.getNode());
	}

	@Test
	public void testContextVariables() {
		// This example also starts from the hello world process.
		// This time even without modification.
		ProcessDefinition processDefinition = ProcessDefinition
				.parseXmlString("<process-definition>" + " <start-state>"
						+ " <transition to='s' />" + " </start-state>"
						+ " <state name='s'>" + " <transition to='end' />"
						+ " </state>" + " <end-state name='end' />"
						+ "</process-definition>");
		ProcessInstance processInstance = new ProcessInstance(processDefinition);
		// Fetch the context instance from the process instance
		// for working with the process variables.
		ContextInstance contextInstance = processInstance.getContextInstance();
		// Before the process has left the start-state,
		// we are going to set some process variables in the
		// context of the process instance.
		contextInstance.setVariable("amount", new Integer(500));
		contextInstance.setVariable("reason", "i met my deadline");
		// From now on, these variables are associated with the
		// process instance. The process variables are now accessible
		// by user code via the API shown here, but also in the actions
		// and node implementations. The process variables are also
		// stored into the database as a part of the process instance.
		processInstance.signal();
		// The variables are accessible via the contextInstance.
		assertEquals(new Integer(500), contextInstance.getVariable("amount"));
		assertEquals("i met my deadline", contextInstance.getVariable("reason"));
	}

	@Test
	public void testHelloWorldProcess() {
		// This method shows a process definition and one execution
		// of the process definition. The process definition has
		// 3 nodes: an unnamed start-state, a state 's' and an
		// end-state named 'end'.
		// The next line parses a piece of xml text into a
		// ProcessDefinition. A ProcessDefinition is the formal
		// description of a process represented as a java object.
		ProcessDefinition processDefinition = ProcessDefinition
				.parseXmlString("<process-definition>" + " <start-state>"
						+ " <transition to='s' />" + " </start-state>"
						+ " <state name='s'>" + " <transition to='end' />"
						+ " </state>" + " <end-state name='end' />"
						+ "</process-definition>");
		// The next line creates one execution of the process definition.
		// After construction, the process execution has one main path
		// of execution (=the root token) that is positioned in the
		// start-state.
		ProcessInstance processInstance = new ProcessInstance(processDefinition);
		// After construction, the process execution has one main path
		// of execution (=the root token).
		Token token = processInstance.getRootToken();
		// Also after construction, the main path of execution is positioned
		// in the start-state of the process definition.
		assertSame(processDefinition.getStartState(), token.getNode());
		// Let's start the process execution, leaving the start-state
		// over its default transition.
		token.signal();
		// The signal method will block until the process execution
		// enters a wait state.
		// The process execution will have entered the first wait state
		// in state 's'. So the main path of execution is now
		// positioned in state 's'
		assertSame(processDefinition.getNode("s"), token.getNode());
		// Let's send another signal. This will resume execution by
		// leaving the state 's' over its default transition.
		token.signal();
		// Now the signal method returned because the process instance
		// has arrived in the end-state.
		assertSame(processDefinition.getNode("end"), token.getNode());
	}
}
