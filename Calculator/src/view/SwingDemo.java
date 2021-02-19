package view;

import javax.swing.JFrame;

public class SwingDemo {
	private static void createGUI()
	{
		// JFrame指一个窗口，构造方法的参数为窗口标题
		// 语法：因为MyFrame是JFrame的子类，所以可以这么写
		JFrame frame = new MyFrame("科学计算器");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		// 设置窗口的其他参数，如窗口大小
		frame.setSize(500, 500);
		frame.setLocation(500,200);
		// 显示窗口
		frame.setVisible(true);
		
	}
	
	public static void main(String[] args)
	{
		// 间接地调用了 createGUI()
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				createGUI();
			}
		});

	}
}
