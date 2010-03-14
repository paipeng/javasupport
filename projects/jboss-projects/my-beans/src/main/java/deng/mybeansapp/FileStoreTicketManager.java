package deng.mybeansapp;

import java.util.*;

public class FileStoreTicketManager {
  private Map<Long, Ticket> store = new HashMap<Long, Ticket>();
  
  public void save(Ticket ticket) {
    ticket.setId(new Long(store.size() + 1));
    store.put(ticket.getId(), ticket);
  }
  
  public Ticket get(Long id) {
    return store.get(id);
  }
  
  public java.util.List<Ticket> getAll() {
    return new ArrayList(store.values());  
  }
}