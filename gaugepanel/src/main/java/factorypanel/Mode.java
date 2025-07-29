package factorypanel;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import code.JSList;

public interface Mode {

    void setMode();
    boolean is();
    String name();
    
    public default String title() {
        return name().substring(0, 1) + name().substring(1).toLowerCase();
    }

    public static enum SIDE implements Mode {
        FACTORY, JEI;

        public static SIDE mode = FACTORY;

        @Override
        public void setMode() {
            SIDE.mode = this;
        }

        @Override
        public boolean is() {
            return this == mode;    
        }
    }

    public static enum SEARCH implements Mode {
        ITEM, ID, MOD;

        public static SEARCH mode = ITEM;

        @Override
        public void setMode() {
            SEARCH.mode = this;
        }

        @Override
        public boolean is() {
            return this == mode;
        }
    }

    public static enum FACTORY implements Mode {
        DRAG, BOX;

        public static FACTORY mode = DRAG;

        @Override
        public void setMode() {
            FACTORY.mode = this;
        }

        @Override
        public boolean is() {
            return this == mode;
        }
    }

    public static enum BOX implements Mode {
        ADD, MOVE, EDIT, REMOVE;

        public static BOX mode = ADD;

        @Override
        public void setMode() {
            BOX.mode = this;
        }

        @Override
        public boolean is() {
            return this == mode;
        }
    }

    public static enum Modes {
        Side(SIDE.values()),
        Search(SEARCH.values()),
        Factory(FACTORY.values()),
        Box(BOX.values()); 

        private final JSList<Mode> modes;
        private JMenuItem selectedMenuItem = null;
        public static JSList<Modes> list = new JSList<>(values());

        Modes(Mode[] modes) {
            this.modes = new JSList<>(modes);
        }

        private void setSelected(JMenu menu, Mode mode, JMenuItem menuItem) {
            mode.setMode();
            menu.setText(name() + " - " + mode.title());
            if (selectedMenuItem != null) {
                selectedMenuItem.setEnabled(true);
            }
            selectedMenuItem = menuItem;
            menuItem.setEnabled(false);
        }

        public JMenu createMenu() {
            JMenu menu = new JMenu(name());
            modes.forEach(m -> {
                JMenuItem menuItem = new JMenuItem(m.title());
                menuItem.addActionListener(e -> setSelected(menu, m, menuItem));
                menu.add(menuItem);
            });
            setSelected(menu, modes.first(), menu.getItem(0));
            return menu;
        }

    }

}
