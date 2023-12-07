package Java_project_1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Day15_MemberDB.MainView;

public class Login extends Frame implements ActionListener{
	
	String url = "jdbc:mysql://localhost:3306/java_db?useUnicode=true&characterEncoding=utf8";
	String id = "root";
	String pass = "qwer";
	private Connection dc;
	
	Font font17 = new Font("TimesRoman", Font.BOLD, 17);
	Font font15 = new Font("TimesRoman", Font.BOLD, 15);
	Font font12 = new Font("TimesRoman", Font.BOLD, 12);
	
	Label Title = new Label("로그인");
	Label joinid = new Label("아이디 : ");
	Label pw = new Label("비밀번호 : ");
	TextField idt = new TextField();
	TextField pwt = new TextField();
	Button login  = new Button("로그인");
	Button join  = new Button("회원가입");

	Login(){
		super("로그인");
		this.setSize(300, 360);
		this.setLocation(700, 350);
		initi();
		start();
		this.setVisible(true);
		
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
		} catch (ClassNotFoundException ee) {
			return;
		}
		try {
			dc = DriverManager.getConnection(url, id, pass);
		} catch (SQLException ee) {
		}
	}
	void initi() {
		this.setLayout(null);
		this.add(Title); this.add(joinid); this.add(pw); this.add(idt);
		this.add(pwt); this.add(login); this.add(join);
		
		Title.setFont(font17);
		
		Title.setBounds(120, 60, 200, 30);
		joinid.setBounds(75, 120, 50, 30);
		idt.setBounds(130, 120, 100, 30);
		pw.setBounds(65, 170, 60, 30);
		pwt.setBounds(130, 170, 100, 30);
		login.setBounds(100, 230, 100, 30);
		join.setBounds(100, 270, 100, 30);
		
		
	}
	void start() {
		login.addActionListener(this); join.addActionListener(this);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean login1 = false;
		String joinid = idt.getText();
		String pw = pwt.getText();
		if(e.getSource()==login) {
			if(joinid.equals("")) {msgDlg("아이디를 입력하세요"); return;}
			if(pw.equals("")) {msgDlg("비밀번호를 입력하세요"); return;}
			if(joinid.equals("admin")&&pw.equals("admin")) {
			this.setVisible(false);
			Main main = new Main();}
			else {
				
				String query = "select*from tb_javaproject where id=? and pw=?";
				try {
					PreparedStatement pstmt = dc.prepareStatement(query);
					pstmt.setString(1, joinid);
					pstmt.setString(2, pw);
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {
						//로그인 성공시 처리
						msgDlg("로그인성공");
		        		Main2 ma = new Main2();
		        		this.setVisible(false);
		        		login1 = true;
		        		
					}
					rs.close();
					pstmt.close();
				} catch (SQLException ee) {
					System.err.println("로그인 실패!! : " + ee.toString());
				}
				if(login1 == false) {msgDlg("로그인실패");}
			}

		}
		else if(e.getSource()==join) {Join join = new Join();}
	}
	
	void msgDlg(String msg)
	{
		//다이얼로그창
    	Dialog dlg = new Dialog(this, "알림창", true);
    	//컴포넌트선언
    	Button bt = new Button("확인");
    	Label lb = new Label(msg);
    	//레이아웃설정
		dlg.setLayout(null);
		//컴포넌트 다이얼로그에 등록
		dlg.add(bt); dlg.add(lb);
	
		//배치하기   		
		lb.setBounds(110, 70, 150, 30);
		bt.setBounds(140, 120, 100, 30);
		
		//이벤트등록
		bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlg.setVisible(false);
			}
		});	
		//이벤트등록
		dlg.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dlg.setVisible(false);
			}
		});
		dlg.setLocation(650, 400);
		dlg.setSize(400, 200);
		dlg.setVisible(true);
	}
}

