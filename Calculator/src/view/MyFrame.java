package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import algorithm.Calculation;
import algorithm.Rule;
import tool.CalculateException;
import tool.GGBorder;
import tool.GGColumnLayout;


public class MyFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	boolean calHistory=false;
	double answer;//上一个算式的答案
	String[] strs= {"log","Ans","ln","arcsin","arccos","arctan","arccot","sin","cos","tan","cot"};

	
	public MyFrame(String title) {
		super(title);
		
		ImageIcon icon=new ImageIcon(getClass().getResource("/images/cal.png"));
		this.setIconImage(icon.getImage());
		// Content Pane
		JPanel root = new JPanel();
		this.setContentPane(root);
		GGBorder.addMargin(root, 10);
		root.setLayout(new GGColumnLayout(0));
		
		//输入文本框
		JTextArea inputArea=creatInputArea(2,0);
		JScrollPane si = new JScrollPane(inputArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		si.setBorder(null);
		root.add(si,"auto");
		
		//结果文本框
		JTextField resultField=creatResultField();
		resultField.setBorder(null);
		GGBorder.addPadding(resultField,10);
		resultField.setHorizontalAlignment(JTextField.RIGHT);
		resultField.setEditable(false);
		resultField.setBackground(Color.white);
		root.add(resultField,"auto");
		
		
		//按钮
		JPanel g = new JPanel();
		GGBorder.addMargin(g, 5, 0, 0, 0);
		GridBagLayout gbaglayout=new GridBagLayout();
		g.setLayout(gbaglayout);
		g.setOpaque(false);
		GridBagConstraints constraints=new GridBagConstraints();
		constraints.fill=GridBagConstraints.BOTH;
		
		constraints.weightx=1;    
        constraints.weighty=1;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        
        JButton button00 = new JButton();
        button00.setBackground(Color.white);
        button00.setFocusPainted(false);
        gbaglayout.setConstraints(button00,constraints);
        g.add(button00);
        JButton button10=makeButton("Π",g,gbaglayout,constraints);    
        JButton button20=makeButton("e",g,gbaglayout,constraints);
        JButton button30=makeButton("DEL",g,gbaglayout,constraints);
        constraints.gridwidth=GridBagConstraints.REMAINDER;    
        JButton button40=makeButton("AC",g,gbaglayout,constraints);
        constraints.gridwidth=1;
        
        JButton button01=makeButton("三角学",g,gbaglayout,constraints);
        JButton button11=makeButton("(",g,gbaglayout,constraints);
        JButton button21=makeButton(")",g,gbaglayout,constraints);
        JButton button31=makeButton("^",g,gbaglayout,constraints);
        constraints.gridwidth=GridBagConstraints.REMAINDER;
        JButton button41=makeButton("/",g,gbaglayout,constraints);
        constraints.gridwidth=1;

        JButton button02=makeButton("n!",g,gbaglayout,constraints);
        JButton button12=makeButton("7",g,gbaglayout,constraints);
        JButton button22=makeButton("8",g,gbaglayout,constraints);
        JButton button32=makeButton("9",g,gbaglayout,constraints);
        constraints.gridwidth=GridBagConstraints.REMAINDER;
        JButton button42=makeButton("*",g,gbaglayout,constraints);
        constraints.gridwidth=1;
		
        JButton button03=makeButton("1/x",g,gbaglayout,constraints);
        JButton button13=makeButton("4",g,gbaglayout,constraints);
        JButton button23=makeButton("5",g,gbaglayout,constraints);
        JButton button33=makeButton("6",g,gbaglayout,constraints);
        constraints.gridwidth=GridBagConstraints.REMAINDER;
        JButton button43=makeButton("-",g,gbaglayout,constraints);
        constraints.gridwidth=1;
        
        JButton button04=makeButton("log",g,gbaglayout,constraints);
        JButton button14=makeButton("1",g,gbaglayout,constraints);
        JButton button24=makeButton("2",g,gbaglayout,constraints);
        JButton button34=makeButton("3",g,gbaglayout,constraints);
        constraints.gridwidth=GridBagConstraints.REMAINDER;
        JButton button44=makeButton("+",g,gbaglayout,constraints);
        constraints.gridwidth=1;
        
        JButton button05=makeButton("ln",g,gbaglayout,constraints);
        JButton button15=makeButton("0",g,gbaglayout,constraints);
        JButton button25=makeButton(".",g,gbaglayout,constraints);
        JButton button35=makeButton("Ans",g,gbaglayout,constraints);
        constraints.gridwidth=GridBagConstraints.REMAINDER;
        JButton button45=makeButton("=",g,gbaglayout,constraints);
        constraints.gridwidth=1;
        
		root.add(g,"1w");
		
		//为按钮加监听器
		addListener(inputArea,button10,"Π");
		addListener(inputArea,button20,"e");
		button30.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				int p=inputArea.getCaretPosition();
				boolean delete=false;
				//如果有选中区域，则删除选中区域
					if(inputArea.getSelectedText()!=null) {
						inputArea.replaceSelection(null);
						delete=true;
					}
				
				
				if(p>0&&!delete) {
					
					StringBuilder text=new StringBuilder(inputArea.getText());
					for(int i=0;i<strs.length && !delete;i++) {
						//先看光标前面是否为特定字符串
						if(p-strs[i].length()<0) continue;
						
						if(text.lastIndexOf(strs[i],p-1)>=0 && text.lastIndexOf(strs[i],p-1)==(p-strs[i].length())) {
							text.delete(p-strs[i].length(), p);
							inputArea.setText(text.toString());
							inputArea.setCaretPosition(p-strs[i].length());
							delete=true;//设置已经删除过一次
						}
					}
					//如果未删除字符串，则删除一个字符
					if(!delete) {
						text.deleteCharAt(p-1);
						inputArea.setText(text.toString());
						inputArea.setCaretPosition(p-1);
					}
					
					
				}
				inputArea.requestFocus();
				
			}
			
		});
		
		button40.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				inputArea.setText("");
				resultField.setText("");
				inputArea.requestFocus();
			}
			
		});
		
		addListener(inputArea,button11,"(");
		addListener(inputArea,button21,")");
		addListener(inputArea,button31,"^");
		addListener(inputArea,button41,"/");
		
		addListener(inputArea,button02,"!");
		addListener(inputArea,button12,"7");
		addListener(inputArea,button22,"8");
		addListener(inputArea,button32,"9");
		addListener(inputArea,button42,"*");
		
		addListener2(inputArea,button03,"1/");
		addListener(inputArea,button13,"4");
		addListener(inputArea,button23,"5");
		addListener(inputArea,button33,"6");
		addListener(inputArea,button43,"-");
		
		addListener2(inputArea,button04,"log");
		addListener(inputArea,button14,"1");
		addListener(inputArea,button24,"2");
		addListener(inputArea,button34,"3");
		addListener(inputArea,button44,"+");
		
		addListener2(inputArea,button05,"ln");
		addListener(inputArea,button15,"0");
		addListener(inputArea,button25,".");
		addListener(inputArea,button35,"Ans");
		
		
		
		//设置编辑inputArea时，resultField清空
		inputArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				if(!resultField.getText().equals(null)) {
					resultField.setText(null);
				}
				
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if(!resultField.getText().equals(null)) {
					resultField.setText(null);
				}
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				
			}
			
		});
		
		
		
		//设置菜单按钮
		String imagePath = "/images/menu.png" ;
		URL imageURL = getClass().getResource(imagePath);
		button00.setIcon(new ImageIcon(imageURL));
		button00.setToolTipText("菜单");
		button00.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				setDecimal();
				
			}

			
		});
		
		
		//设置三角函数按钮
		
		JPopupMenu popupMenuTwo =new JPopupMenu();
		@SuppressWarnings("unused")
		JMenuItem sin=creatMenuItem("sin",popupMenuTwo,inputArea);
		@SuppressWarnings("unused")
		JMenuItem cos=creatMenuItem("cos",popupMenuTwo,inputArea);
		@SuppressWarnings("unused")
		JMenuItem tan=creatMenuItem("tan",popupMenuTwo,inputArea);
		@SuppressWarnings("unused")
		JMenuItem arcsin=creatMenuItem("arcsin",popupMenuTwo,inputArea);
		@SuppressWarnings("unused")
		JMenuItem arccos=creatMenuItem("arccos",popupMenuTwo,inputArea);
		@SuppressWarnings("unused")
		JMenuItem arctan=creatMenuItem("arctan",popupMenuTwo,inputArea);
		button01.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				popupMenuTwo.show(e.getComponent(),button01.getLocation().x+button01.getSize().width,button00.getLocation().y);
			}
			
		});
		
		
		
		button45.addMouseListener(new MouseAdapter() {
	
			@Override
			public void mouseClicked(MouseEvent e) {
					
				try {
					if(inputArea.getText().equals("")) {inputArea.requestFocus();return;}
						
					double resultNumber=Calculation.calculate(inputArea.getText(),calHistory, answer);
					int digits=Rule.getReservedDecimal();//保留小数位数
					
						
					@SuppressWarnings("deprecation")
					double result= new BigDecimal(resultNumber).setScale(digits,BigDecimal.ROUND_HALF_UP).doubleValue();
					if(!calHistory) calHistory=true;
					answer=result;
					if(Math.round(result)-result==0) {
						resultField.setText("="+(long)result);
					}else {
						resultField.setText("="+result);
					}
				}catch(CalculateException exception) {
//					resultField.setText("ERROR"+exception.getMessage());
					resultField.setText("ERROR");
				}
			}
			
		});
		
		
		//按回车或"="键出结果
		inputArea.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
				if(e.getSource()==inputArea) {
					if(e.getKeyCode()==KeyEvent.VK_ENTER||e.getKeyCode()==KeyEvent.VK_EQUALS) {
						button45.doClick();
						
						String t=inputArea.getText();
						int i=t.length();
						while(i>0&&(t.charAt(i-1)=='\n'||t.charAt(i-1)=='=')) {
							i--;
						}
						
						inputArea.setText(t.substring(0, i));

						try {
							if(inputArea.getText().isEmpty()) {inputArea.requestFocus();return;}
								
							double resultNumber=Calculation.calculate(inputArea.getText(),calHistory, answer);
							int digits=Rule.getReservedDecimal();//保留小数位数
							
							
							@SuppressWarnings("deprecation")
							double result= new BigDecimal(resultNumber).setScale(digits,BigDecimal.ROUND_HALF_UP).doubleValue();
							if(!calHistory) calHistory=true;
							answer=result;
							if(Math.round(result)-result==0) {
								resultField.setText("="+(long)result);
							}else {
								resultField.setText("="+result);
							}
						}catch(CalculateException exception) {
//							resultField.setText("ERROR"+exception.getMessage());
							resultField.setText("ERROR");
						}
					}else if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
						button40.doClick();
						inputArea.setText("");
						resultField.setText("");
						inputArea.requestFocus();
					}
				}
				
				
			}
			
		});
		
	}

	
	private void setDecimal() {
		boolean flag=true;
		String number=null;
		
		label:while(flag) {

			URL imageURL = getClass().getResource("/images/set.png");
			ImageIcon icon=new ImageIcon(imageURL);
			Object input = JOptionPane.showInputDialog(this,"设置保留小数位数:","设置",JOptionPane.INFORMATION_MESSAGE,icon,null,Rule.getReservedDecimal());
			
			if(input==null)
				return;
			else
				number=(String)input;
			if(number.length()==0) {
				continue;
			}
			
			char[] nums=number.toCharArray();
			for(char c:nums) {
				if(c<'1'||c>'9')
					continue label;
			}
			break;
		}
		
		Rule.setReservedDecimal(Integer.parseInt(number));
		
		return;
	}
	
	private JMenuItem creatMenuItem(String string, JPopupMenu popupMenuTwo,JTextArea inputArea) {
		JMenuItem menuItem=new JMenuItem(string);
		menuItem.setFont(new Font("黑体",0,28));
		menuItem.setActionCommand(string);
		popupMenuTwo.add(menuItem);
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				inputArea.replaceSelection(string+"()");
				inputArea.setCaretPosition(inputArea.getCaretPosition()-1);
				inputArea.requestFocus();
				
			}
			
		});
		
		return menuItem;
	}

	private JTextArea creatInputArea(int rows,int columns) {
		
		JTextArea inputArea = new JTextArea(rows,columns);
		inputArea.setFont(new Font("黑体",0,28));
		GGBorder.addPadding(inputArea,10);
		return inputArea;
	}
	
	private JTextField creatResultField() {
		
		JTextField resultField = new JTextField();
		resultField.setFont(new Font("黑体",0,28));
		return resultField;
	}
	
	public static JButton makeButton(String title,JPanel frame,GridBagLayout gridBagLayout,GridBagConstraints constraints) {
        JButton button=new JButton(title);
        button.setFont(new Font("黑体",0,28));
        button.setBackground(Color.white);
        button.setFocusPainted(false);
        gridBagLayout.setConstraints(button,constraints);
        frame.add(button);
        
        return button;
    }
	
	/**
	 * @Description:输入str
	 * @author Wang ZH
	 */
	private void addListener(JTextArea a,JButton button,String str) {
		button.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				a.replaceSelection(str);
				
				a.requestFocus();
			}
			
		});
	}

	
	/**
	 * @Description:输入str后再加两个括号
	 * @author Wang ZH
	 */
	private void addListener2(JTextArea a,JButton button,String str) {
		button.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				a.replaceSelection(str+"()");
				a.setCaretPosition(a.getCaretPosition()-1);
				a.requestFocus();
			}
			
		});
	}
}
