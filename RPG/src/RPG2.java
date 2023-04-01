import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
public class RPG2 extends JFrame implements ActionListener{
	static playableStatus mainStatus = new playableStatus();
	//static itemBag itembag = new itemBag();
	static equipment equipments = new equipment();
	static itemBagJFrame iBJ = new itemBagJFrame();
	static eventList Event = new eventList();
	public static void main(String args[]){
		mainStatus.statusSet(1,100,100,5);
		mainStatus.PStatusSet(100,0,1,0);
		refresh();
		Event.executeEvent(Event.selectEvent(30));
		new RPG2("RPG");
	}
	static String EnemyName = "";
	static boolean fight = false;
	static boolean trun = false;
	static int pos=1;
	static int risk=100;
	JButton next = new JButton("前に進む");
	static JButton heal = new JButton("回復:"+iBJ.BAG.healItem.remaining);
	JButton GiveUp = new JButton("あきらめる");
	static JTextField Lvtxt = new JTextField("Lv."+mainStatus.Lv+"("+mainStatus.nextLv+")");
	static JTextField HPtxt = new JTextField("HP"+mainStatus.maxHP+"/"+mainStatus.nowHP);
	static JTextField Powtxt = new JTextField("Power"+mainStatus.Pow);
	public static JTextArea log = new JTextArea(5,20);
	JScrollPane scroll = new JScrollPane(log,
	JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	JButton Attack = new JButton("攻撃");
	JButton charge = new JButton("ためる");
	JButton itemBag = new JButton("アイテム");
	static int[] EnemyStatus=Enemy(0);
	RPG2(String title){
		setTitle(title);
		log.setLineWrap(true);
		log.setEditable(false);
		HPtxt.setEditable(false);
		Lvtxt.setEditable(false);
		Powtxt.setEditable(false);
		JPanel jpN = new JPanel();
		JPanel jpE = new JPanel();
		JPanel jpS = new JPanel();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jpN,BorderLayout.NORTH);
		getContentPane().add(jpS,BorderLayout.SOUTH);
		getContentPane().add(jpE,BorderLayout.EAST);
		jpN.add(next);
		jpN.add(heal);
		jpN.add(GiveUp);
		getContentPane().add(scroll,BorderLayout.CENTER);
		jpS.add(Lvtxt);
		jpS.add(HPtxt);
		jpS.add(Powtxt);
		jpE.setLayout(new BoxLayout(jpE,BoxLayout.Y_AXIS));
		jpE.add(Attack);
		jpE.add(charge);
		jpE.add(itemBag);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(450,300);
		setVisible(true);
		next.addActionListener(this);
		heal.addActionListener(this);
		Attack.addActionListener(this);
		itemBag.addActionListener(this);
	}
	public static void walk(int steps){
		if(fight){
			log.append("敵がいて前に進めそうにない\n");
		}else{
			log.append("area"+pos+"→"+"area"+(pos+1)+"に進んだ("+steps+")\n");
			pos+=steps;
			Event.executeEvent(Event.selectEvent(risk));
			//event();
		}
		risk+=steps;
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==next){
			walk(1);
		}else if(e.getSource()==heal){
			if(iBJ.BAG.healItem.remaining>0){
				iBJ.BAG.healItem.remaining--;
				log.append("ポーションを使って回復した、残りは"+iBJ.BAG.healItem.remaining+"\n");
				mainStatus.nowHP+=(mainStatus.maxHP-mainStatus.nowHP)/2+10;
				if(mainStatus.nowHP>mainStatus.maxHP){
					mainStatus.nowHP=mainStatus.maxHP;
				}
			}else{
				log.append("ポーションはもうない\n");
			}
			if(fight){
				Damage(0);
			}
		}else if(e.getSource()==Attack){
			if(fight){
				EnemyStatus[2]-=mainStatus.Pow;
				log.append(EnemyName+"に"+mainStatus.Pow+"のダメージ\n");
				if(EnemyStatus[2]<=0){
					log.append(EnemyName+"を倒した\n");
					mainStatus.nextLv-=EnemyStatus[0]*5;
					fight=false;
				}else{
					Damage(0);
				}
			}else{
				log.append("攻撃はむなしく空ぶった\n");
			}
		}else if(e.getSource()==itemBag){
			iBJ.openItemBag();
		}
		refresh();
	}
	public static void battle(int EnemyTag){
		fight = true;
		EnemyStatus = Enemy(EnemyTag);
		logWrite(EnemyName);
	}
	public static int[] Enemy(int tag){
		int LvRange = 0,PowRange = 0,HPRange = 0;
		int addLv = 0,addPow = 0,addHP = 0;
		double Rand = Math.random();
		switch(tag){
			case 0:
			EnemyName = "スライム";
			LvRange = 1;
			PowRange = 2;
			HPRange = 4;
			addLv = (int)(Rand*(LvRange+1));
			addPow = (int)(Rand*(PowRange+1));
			addHP = (int)(Rand*(HPRange+1));
			return new int[]{1+addLv,12+addHP,12+addHP,3+addPow};
			case 1:
			EnemyName = "ゴブリン";
			LvRange = 4;
			PowRange = 5;
			HPRange = 2;
			addLv = (int)(Rand*(LvRange+1));
			addPow = (int)(Rand*(PowRange+1));
			addHP = (int)(Rand*(HPRange+1));
			return new int[]{4+addLv,15+addHP,15+addHP,10+addPow};
			case 100:
			EnemyName = "死";
			addLv = (int)(Rand*(LvRange+1));
			addPow = (int)(Rand*(PowRange+1));
			addHP = (int)(Rand*(HPRange+1));
			return new int[]{999+addLv,1000+addHP,1000+addHP,5000+addPow};
		}
		return new int[]{1,12,12,3};
	}
	public static void Damage(int damagetype){
		switch(damagetype){
			case 0:
			mainStatus.nowHP -= EnemyStatus[3];
			log.append(EnemyName+"から"+EnemyStatus[3]+"のダメージ\n");
			refresh();
			break;
		}
		if(mainStatus.nowHP<=0){
			log.append("死んだ\n");
			System.exit(1);
		}
	}
	static void fixedHeal(int healSize){
		mainStatus.nowHP += healSize;
		refresh();
		System.out.println("test"+mainStatus.nowHP);
	}
	static void percentageHeal(int percentage){
		mainStatus.nowHP += (int)(mainStatus.maxHP*((double)(percentage)/100));
		refresh();
		System.out.println("test"+mainStatus.nowHP);
	}
	public static void refresh(){
		if(mainStatus.nowHP > mainStatus.maxHP){
			mainStatus.nowHP = mainStatus.maxHP;
		}
		Powtxt.setText("Power"+mainStatus.Pow);
		heal.setText("回復:"+iBJ.BAG.healItem.remaining);
		HPtxt.setText("HP"+mainStatus.nowHP+"/"+mainStatus.maxHP);
		Lvtxt.setText("Lv."+mainStatus.Lv+"("+mainStatus.nextLv+")");
	}
	static void logWrite(String MAIN){
		MAIN += "\n";
		log.append(MAIN);
	}
}