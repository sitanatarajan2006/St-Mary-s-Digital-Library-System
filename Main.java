import data.DbConnection;
import data.MemberData;
import libsystem.Members;

public class Main {

    public static void main(String[] args) {

        DbConnection.createTables();

        MemberData memberData = new MemberData();

        Members member = new Members(1, "Alice Johnson", "alice.johnson@stmarys.ac.uk", "Student");

        memberData.addMember(member);

        System.out.println("All members:");

        for (Members m : memberData.getAllMembers()) {
            System.out.println(m.getMemberId() + " | " + m.getMemberName() + " | " + m.getEmail());
        }
    }
}