package Java_project_1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main2 extends Frame implements ActionListener{

	String url = "jdbc:mysql://localhost:3306/java_db?useUnicode=true&characterEncoding=utf8";
	String id = "root";
	String pass = "qwer";
	private Connection dc;
	
		Font font20 = new Font("TimesRoman", Font.BOLD, 20);
		Font font13 = new Font("TimesRoman", Font.BOLD, 13);
		Font font12b = new Font("TimesRoman", Font.BOLD, 12);
		
		Label name = new Label("도서명 : ");
		Label writer = new Label("저자 : ");
		TextField namet = new TextField();
		TextField writert = new TextField();
		TextArea list = new TextArea();
		Button src  = new Button("검색");
		Button lend  = new Button("도서대여");
		Button ret  = new Button("도서반납");
		Button edit  = new Button("회원정보수정");
		Button close  = new Button("닫기");
		
		Label info = new Label("검색된 도서정보");
		Label inname = new Label("도서명 : ");
		TextField innamet = new TextField();
		Label inwriter = new Label("저자 : ");
		TextField inwritert = new TextField();
		Label publish = new Label("출판사 : ");
		TextField publisht = new TextField();
		Label cord = new Label("분류 : ");
		TextField cordt = new TextField();
		Label stack = new Label("재고 : ");
		Label stackt = new Label();

		Main2(){
			super("도서대여 프로그램");
			this.setSize(700, 460);
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
			DataLoad();
		}
		
		void initi() {
			this.setLayout(null);
			this.add(name); this.add(writer); this.add(namet);  this.add(writert);
			this.add(src); this.add(list); this.add(edit); this.add(close); this.add(lend); this.add(ret);
			this.add(info); this.add(inname); this.add(innamet); this.add(inwriter); this.add(inwritert);
			this.add(publish); this.add(publisht); this.add(cord); this.add(cordt); this.add(stack); this.add(stackt); 
			
			
			name.setFont(font13); writer.setFont(font13); info.setFont(font13); list.setFont(font13);
			
			name.setBounds(100, 50, 60, 30); namet.setBounds(160, 50, 90, 30);
			writer.setBounds(310, 50, 60, 30); writert.setBounds(370, 50, 90, 30);
			src.setBounds(480, 50, 50, 30);
			list.setBounds(190, 100, 470, 280);
			edit.setBounds(190, 400, 100, 30); lend.setBounds(50, 360, 100, 30); ret.setBounds(50, 400, 100, 30); 
			close.setBounds(600, 400, 50, 30);
			info.setBounds(40, 110, 110, 30);
			inname.setBounds(20, 150, 50, 30); innamet.setBounds(80, 150, 90, 25);
			inwriter.setBounds(30, 190, 50, 30); inwritert.setBounds(80, 190, 90, 25);
			publish.setBounds(20, 230, 50, 30); publisht.setBounds(80, 230, 90, 25);
			cord.setBounds(30, 270, 50, 30); cordt.setBounds(80, 270, 90, 25);
			stack.setBounds(30, 310, 50, 30); stackt.setBounds(80, 310, 90, 25);
			
	}
		void close() {this.setVisible(false);}
		void start() {
			src.addActionListener(this); edit.addActionListener(this); close.addActionListener(this);
			lend.addActionListener(this); ret.addActionListener(this);
			
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					close();
				}
			});
		}
		void DataLoad(){
			Statement stmt = null; // 접속상태
			ResultSet rs = null; // db 주머니같은
			String query = "select * from tb_javaproject1"; //쿼리목록

			try {
				stmt = dc.createStatement(); //접속한 상태값
				rs = stmt.executeQuery(query); //끌어와서 실행
				String data="";
				while (rs.next()) {
					data+=rs.getString("name")+"/"+rs.getString("writer")+"/"+rs.getString("publish")+"/"+rs.getString("cord")+rs.getString("stack")+"\n"+"\n";
				}
				list.setText(data);
				rs.close();
				stmt.close();
			} catch (SQLException ee) {
				System.err.println("error = " + ee.toString());
			}
			
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String name = namet.getText();
			String writer = writert.getText();
			if(e.getSource()==src) {
				if(name.equals("")) {msgDlg("도서명을 입력하세요"); return;}
				if(writer.equals("")) {msgDlg("저자를 입력하세요"); return;}
				Search(name, writer);}
			
			else if(e.getSource()==lend) { 
				String stack = "대여중";
				
				if(name.equals("")) {
					msgDlg("대여할 도서를 입력하세요"); return;}
				if(writer.equals("")) {
					msgDlg("대여할 도서의 저자를 입력하세요"); return;}
				
					if (stackt.getText().equals("대여가능")) {
						update(stack, name);
						msgDlg("대여완료");
					} else if (stackt.getText().equals("대여중")) {
						msgDlg("대여불가"); 
					}
					String query = "select * from tb_javaproject1 where name = ? and writer = ?";
					
					try {
						PreparedStatement pstmt = dc.prepareStatement(query);
						pstmt.setString(1, name);
						pstmt.setString(2, writer);
						ResultSet rs = pstmt.executeQuery();
						if (rs.next()) {
							stackt.setText(rs.getString("stack"));
						}
						rs.close();
						pstmt.close();
					} catch (SQLException ee) {}
					
			}
			
			else if(e.getSource()==ret) {
				String stack = "대여가능";
				
				if(name.equals("")) {
					msgDlg("반납할 도서를 입력하세요"); return;}
				if(writer.equals("")) {
					msgDlg("반납할 도서의 저자를 입력하세요"); return;}
				
				if (stackt.getText().equals("대여중")) {
					update(stack, name);
					msgDlg("반납완료");
				} else if (stackt.getText().equals("대여가능")) {
					msgDlg("반납불가"); 
				}
				String query = "select * from tb_javaproject1 where name = ? and writer = ?";
				
				try {
					PreparedStatement pstmt = dc.prepareStatement(query);
					pstmt.setString(1, name);
					pstmt.setString(2, writer);
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {
						stackt.setText(rs.getString("stack"));
					}
					rs.close();
					pstmt.close();
				} catch (SQLException ee) {}
			}	
			

			else if(e.getSource()==edit) {MemberEdit edit = new MemberEdit();}
			else if(e.getSource()==close) {close();}
		}
		
	

		void Search(String name, String writer){
			boolean check=false;
			String query = "select * from tb_javaproject1 where name = ? and writer = ?";
			
			try {
				PreparedStatement pstmt = dc.prepareStatement(query);
				pstmt.setString(1, name);
				pstmt.setString(2, writer);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					check=true;
					innamet.setText(rs.getString("name"));
					inwritert.setText(rs.getString("writer"));
					publisht.setText(rs.getString("publish"));
					cordt.setText(rs.getString("cord"));
					stackt.setText(rs.getString("stack"));
				}
				rs.close();
				pstmt.close();
			} catch (SQLException ee) {
				System.err.println("login 처리 실패!!");
			}
			if(check==false) {msgDlg("도서가 없습니다");}
		}
		
		void update(String stack, String name) {
			String query = "update tb_javaproject1 set stack = ? where name = ?";
			
			try {
				PreparedStatement pstmt = dc.prepareStatement(query);
				pstmt.setString(1, stack);
				pstmt.setString(2, name);
				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException ee) {
				System.err.println("회원 정보수정 실패!!");
			}
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
