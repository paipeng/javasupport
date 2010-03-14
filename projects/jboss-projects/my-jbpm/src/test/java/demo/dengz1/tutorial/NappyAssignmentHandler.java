package demo.dengz1.tutorial;

import org.jbpm.graph.exe.*;
import org.jbpm.taskmgmt.def.*;
import org.jbpm.taskmgmt.exe.Assignable;

public class NappyAssignmentHandler implements AssignmentHandler {

  private static final long serialVersionUID = 1L;

  public void assign(Assignable assignable, ExecutionContext executionContext) {
    assignable.setActorId("papa");
  }

}
