package net.gtidev.test;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class MemoryService {

  private int value;

  private List<MemoryEventListener> listeners = new ArrayList<>();

  public void addListener(MemoryEventListener listener) {
    listeners.add(listener);
  }

  public void removeListener(MemoryEventListener listener) {
    listeners.remove(listener);
  }

  private void fireEvent() {
    for (MemoryEventListener l : listeners) {
      l.valueChanged(value);
    }
  }

  public void setValue(int value) {
    this.value = value;
    fireEvent();
  }
}
