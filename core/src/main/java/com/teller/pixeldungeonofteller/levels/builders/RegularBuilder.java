package com.teller.pixeldungeonofteller.levels.builders;

import com.teller.pixeldungeonofteller.levels.rooms.Room;
import com.teller.pixeldungeonofteller.levels.rooms.connection.ConnectionRoom;
import com.teller.pixeldungeonofteller.levels.rooms.connection.MazeConnectionRoom;
import com.teller.pixeldungeonofteller.levels.rooms.secret.SecretRoom;
import com.teller.pixeldungeonofteller.levels.rooms.special.ShopRoom;
import com.teller.pixeldungeonofteller.levels.rooms.standard.EntranceRoom;
import com.teller.pixeldungeonofteller.levels.rooms.standard.ExitRoom;
import com.teller.pixeldungeonofteller.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class RegularBuilder extends Builder {

    // *** Parameter values for level building logic ***
    // note that implementations do not have to use al of these variables

    protected float pathVariance = 45f;

    public RegularBuilder setPathVariance(float var) {
        pathVariance = var;
        return this;
    }

    //path length is the percentage of pathable rooms that are on
    protected float pathLength = 0.5f;
    //The chance weights for extra rooms to be added to the path
    protected float[] pathLenJitterChances = new float[]{0, 1, 0};

    public RegularBuilder setPathLength(float len, float[] jitter) {
        pathLength = len;
        pathLenJitterChances = jitter;
        return this;
    }

    protected float[] pathTunnelChances = new float[]{1, 3, 1};
    protected float[] branchTunnelChances = new float[]{2, 2, 1};

    public RegularBuilder setTunnelLength(float[] path, float[] branch) {
        pathTunnelChances = path;
        branchTunnelChances = branch;
        return this;
    }

    protected float extraConnectionChance = 0.2f;

    public RegularBuilder setExtraConnectionChance(float chance) {
        extraConnectionChance = chance;
        return this;
    }

    // *** Room Setup ***

    protected Room entrance = null;
    protected Room exit = null;
    protected Room shop = null;

    protected ArrayList<Room> multiConnections = new ArrayList<>();
    protected ArrayList<Room> singleConnections = new ArrayList<>();

    protected void setupRooms(ArrayList<Room> rooms) {
        entrance = exit = shop = null;
        singleConnections.clear();
        multiConnections.clear();
        for (Room r : rooms) {
            if (r instanceof EntranceRoom) {
                entrance = r;
            } else if (r instanceof ExitRoom) {
                exit = r;
            } else if (r instanceof ShopRoom && r.maxConnections(Room.ALL) == 1) {
                shop = r;
            } else if (r.maxConnections(Room.ALL) > 1) {
                multiConnections.add(r);
            } else if (r.maxConnections(Room.ALL) == 1) {
                singleConnections.add(r);
            }
        }
    }

    // *** Branch Placement ***

    protected static void weightRooms(ArrayList<Room> rooms) {
        for (Room r : rooms.toArray(new Room[0])) {
            if (r instanceof StandardRoom) {
                for (int i = 0; i < ((StandardRoom) r).sizeCat.connectionWeight(); i++)
                    rooms.add(r);
            }
        }
    }

    //places the rooms in roomsToBranch into branches from rooms in branchable.
    //note that the three arrays should be separate, they may contain the same rooms however
    protected void createBranches(ArrayList<Room> rooms, ArrayList<Room> branchable,
                                         ArrayList<Room> roomsToBranch, float[] connChances) {

        int i = 0;
        float angle;
        int tries;
        Room curr;
        ArrayList<Room> connectingRoomsThisBranch = new ArrayList<>();
        float[] connectionChances = connChances.clone();
        while (i < roomsToBranch.size()) {

            Room r = roomsToBranch.get(i);

            connectingRoomsThisBranch.clear();

            do {
                curr = Random.element(branchable);
            } while( r instanceof SecretRoom && curr instanceof ConnectionRoom);

            int connectingRooms = Random.chances(connectionChances);
            if (connectingRooms == -1){
                connectionChances = connChances.clone();
                connectingRooms = Random.chances(connectionChances);
            }
            connectionChances[connectingRooms]--;

            for (int j = 0; j < connectingRooms; j++){
                ConnectionRoom t = r instanceof SecretRoom ? new MazeConnectionRoom() : ConnectionRoom.createRoom();
                tries = 3;

                do {
                    angle = placeRoom(rooms, curr, t, randomBranchAngle(curr));
                    tries--;
                } while (angle == -1 && tries > 0);

                if (angle == -1) {
                    for (Room c : connectingRoomsThisBranch){
                        c.clearConnections();
                        rooms.remove(c);
                    }
                    connectingRoomsThisBranch.clear();
                    break;
                } else {
                    connectingRoomsThisBranch.add(t);
                    rooms.add(t);
                }

                curr = t;
            }

            if (connectingRoomsThisBranch.size() != connectingRooms){
                continue;
            }

            tries = 10;

            do {
                angle = placeRoom(rooms, curr, r, randomBranchAngle(curr));
                tries--;
            } while (angle == -1 && tries > 0);

            if (angle == -1){
                for (Room t : connectingRoomsThisBranch){
                    t.clearConnections();
                    rooms.remove(t);
                }
                connectingRoomsThisBranch.clear();
                continue;
            }

            for (int j = 0; j <connectingRoomsThisBranch.size(); j++){
                if (Random.Int(3) <= 1) branchable.add(connectingRoomsThisBranch.get(j));
            }
            if (r.maxConnections(Room.ALL) > 1 && Random.Int(3) == 0) {
                if (r instanceof StandardRoom){
                    for (int j = 0; j < ((StandardRoom) r).sizeCat.connectionWeight(); j++){
                        branchable.add(r);
                    }
                } else {
                    branchable.add(r);
                }
            }

            i++;
        }
    }

    protected float randomBranchAngle( Room r ){
        return Random.Float(360f);
    }
}
