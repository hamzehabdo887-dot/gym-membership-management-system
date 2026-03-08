import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

class Member{
    int memberID;
    String name;
    String email;
    String phone;
    static int idCounter = 1; 

    public Member(String name, String email, String phone) {
        this.memberID =idCounter++;
        this.name=name;
        this.email =email;
        this.phone  =phone;}

    public String toString()  {
        return memberID+ ": " +name + " ("+email +", "+phone +")";}
}

class MembershipPlan{
    String planName;
    int durationMonths;
    double fee;

    public MembershipPlan(String planName,int durationMonths,double fee){
        this.planName= planName;
        this.durationMonths=  durationMonths;
        this.fee = fee;
}

    String getPlanDetails() {
        return planName+ " - "+durationMonths +" months - $"+fee;}
}

class Membership{
    Member member;
    MembershipPlan plan;

    public Membership(Member m ,MembershipPlan p)  {
        member =m;
        plan=p;
    }

    String getDetails(){
        return member+"\nPlan: " +plan.getPlanDetails() +"\nFee: $"+plan.fee; }
}

class GymSystem  {
    ArrayList<Member> members;
    ArrayList<MembershipPlan> plans;
    ArrayList<Membership> memberships;
    static GymSystem instance;

    private GymSystem(){
        members=  new ArrayList<>();
        plans =new ArrayList<>();
        memberships =new ArrayList<>();
     
        plans.add(new MembershipPlan( "1 Month",1, 50));
        plans.add(new MembershipPlan("3 Months", 3,140));
        plans.add(new MembershipPlan( "6 Months" ,6, 270));
        plans.add(new MembershipPlan("12 Months",12 ,500 ));}
    static GymSystem getInstance(){
        if(instance==null) instance=new GymSystem();
        return instance;}

    Member registerMember(String name , String email,String phone){
        for(int i= 0;i<members.size();i++){
            Member m=members.get(i);
            if(m.email.equalsIgnoreCase(email) || m.phone.equals(phone)){
                JOptionPane.showMessageDialog(null,"Email or phone already exists");
                return null;
            }
        }
        if(name.isEmpty() || email.isEmpty() || phone.isEmpty()){
           
            JOptionPane.showMessageDialog(null,"All fields required");
            return null;
        }
        Member newM =new Member(name,email,phone);
        members.add(newM);
        return newM;
    }

    Membership assignMembership(Member member,MembershipPlan plan){
        if(member== null || plan==null) return null; 
        Membership m =new Membership(member,plan);
        memberships.add(m);
        return m;
    }

    Membership findMembershipByMemberID(int id){
        for(Membership m : memberships){
            if(m.member.memberID== id) return m;
        }
        return null;
    }

    MembershipPlan findPlanByDetails(String details){
        for(int j=0;j<plans.size();j++){
            MembershipPlan p = plans.get(j);
            if(p.getPlanDetails().equals(details)) return p;}
        return null;}

    void showAllMemberships(){
        String all="";
        for(int i=0;i<memberships.size();i++){
            Membership m = memberships.get(i);
            all += m.getDetails() + "\n\n";}
        JOptionPane.showMessageDialog(null,all);
    }
}

public class GymApp {
    JFrame frame;
    GymSystem system;

    GymApp(){
        system=GymSystem.getInstance();
        frame =new JFrame( "Gym Management System");
        frame.setSize( 450, 450);
        frame.setLayout(new GridLayout( 6,1));
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
        JButton regBtn= new JButton("Register Member");
        JButton assignBtn =new JButton( "Assign Membership");
        JButton displayBtn= new JButton( "Display Membership");
        JButton updateBtn=new JButton("Update Member Info" );
        JButton createPlanBtn= new JButton( "Create Plan");
        JButton exitBtn =new JButton( "Exit");
        frame.add(regBtn);frame.add(createPlanBtn);frame.add(assignBtn);
        frame.add(displayBtn); frame.add(updateBtn);frame.add(exitBtn);

        regBtn.addActionListener(e->{
            String name =JOptionPane.showInputDialog("Enter name:");
            String email =JOptionPane.showInputDialog( "Enter email:");
            String phone= JOptionPane.showInputDialog("Enter phone:" );
            Member m =system.registerMember( name,email,phone );
            if(m!=null) JOptionPane.showMessageDialog( null, "Registered: "+m);
        });

        createPlanBtn.addActionListener(e->{
            String name =JOptionPane.showInputDialog("Plan name:");
            int dur=0;
            double fee=0;
            try{
                dur=Integer.parseInt(JOptionPane.showInputDialog("Months:"));
                fee=Double.parseDouble(JOptionPane.showInputDialog("Fee:"));
            }catch(Exception ex){return;}
            MembershipPlan p= new MembershipPlan(name,dur,fee);
            system.plans.add(p);
            JOptionPane.showMessageDialog(null,"Plan added: "+p.getPlanDetails());
        });

        assignBtn.addActionListener(e->{
            int id=0;
            try{id=Integer.parseInt(JOptionPane.showInputDialog("Member ID:"));}catch(Exception ex){return;}
            Member member=null;
            for(Member m: system.members){if(m.memberID==id) member=m;}
            if(member==null) return;
            String[] options = new String[system.plans.size()];
            for(int j=0;j<system.plans.size();j++) options[j]=system.plans.get(j).getPlanDetails();
            String sel = (String) JOptionPane.showInputDialog(null,"Select plan:","Plans",JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
            MembershipPlan plan = system.findPlanByDetails(sel);
            system.assignMembership(member,plan);
        });

        displayBtn.addActionListener(e->{
            int id=0;
            try{id=Integer.parseInt(JOptionPane.showInputDialog("Member ID:"));}catch(Exception ex){return;}
            Membership m = system.findMembershipByMemberID(id);
            if(m!=null) JOptionPane.showMessageDialog(null,m.getDetails());
        });

        updateBtn.addActionListener(e->{
            int id=0;
            try{id=Integer.parseInt(JOptionPane.showInputDialog("Member ID:"));}catch(Exception ex){return;}
            Member member=null;
            for(Member m:system.members){if(m.memberID==id)member=m;}
            if(member==null) return;
            String name = JOptionPane.showInputDialog("New name:",member.name);
            String email = JOptionPane.showInputDialog("New email:",member.email);
            String phone = JOptionPane.showInputDialog("New phone:",member.phone);
            member.name=name;
            member.email=email;
            member.phone=phone;
        });

        exitBtn.addActionListener(e->{System.exit(0);});
        frame.setVisible(true);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(GymApp::new);
    }
}