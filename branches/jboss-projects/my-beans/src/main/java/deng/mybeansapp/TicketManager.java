package deng.mybeansapp;

public interface TicketManager {
  public void save(Ticket ticket);
  public Ticket get(Long id);
  public java.util.List<Ticket> getAll();
}