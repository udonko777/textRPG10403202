package textRPG10403202;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import textRPG10403202.items.Heal_Low;
public class itemBagJFrame extends JFrame implements ActionListener {
	itemBag BAG;
	JTextField[] itemtxt = new JTextField[20];
	JButton selectedUp = new JButton("↑");
	JButton selectedDown = new JButton("↓");
	JButton useItem = new JButton("使う");
	int selectedBagNumber = 0;

	itemBagJFrame(itemBag ib) {
		BAG = ib;
		BAG.setEmpty();
		BAG.setItem(0, new Heal_Low(40));
		BAG.setHealItem(0);
		BAG.addItem(new Heal_Low(40));
		BAG.addItem(new Heal_Low(40));

		JPanel bwjpC = new JPanel();
		JPanel bwjpE = new JPanel();

		getContentPane().add(bwjpC, BorderLayout.CENTER);
		getContentPane().add(bwjpE, BorderLayout.EAST);

		bwjpC.setLayout(new BoxLayout(bwjpC, BoxLayout.Y_AXIS));
		bwjpE.setLayout(new BoxLayout(bwjpE, BoxLayout.Y_AXIS));

		for (int i = 0; i < BAG.items.length; i++) {
			itemtxt[i] = new JTextField(BAG.items[i].getItemName());
			itemtxt[i].setEditable(false);
			bwjpC.add(itemtxt[i]);
		}

		bwjpE.add(selectedUp);
		bwjpE.add(selectedDown);
		bwjpE.add(useItem);

		useItem.addActionListener(this);
		selectedUp.addActionListener(this);
		selectedDown.addActionListener(this);

		selectedItem();
		pack();
		setSize(450, getSize().height);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == selectedUp) {
			selectedBagNumber--;
			System.out.println(selectedBagNumber);
		} else if (e.getSource() == selectedDown) {
			selectedBagNumber++;
		} else if (e.getSource() == useItem) {
			BAG.items[selectedBagNumber].use(BAG.owner);
			if (BAG.items[selectedBagNumber].getdurability() == 0) {
				BAG.breakItem(selectedBagNumber);
			}
		}

		selectedItem();
	}

	void selectedItem() {

		if (selectedBagNumber < 0) {
			selectedBagNumber = itemtxt.length - 1;
		} else if (selectedBagNumber >= itemtxt.length) {
			selectedBagNumber = 0;
		}

		for (int i = 0; i < itemtxt.length; i++) {
			itemtxt[i].setBackground(new Color(255, 255, 255));
		}
		
		itemtxt[selectedBagNumber].setBackground(new Color(128, 128, 255));
		itemtxt[selectedBagNumber].setText(BAG.items[selectedBagNumber].getItemName());
	}

	void openItemBag() {
		setVisible(true);
	}
}