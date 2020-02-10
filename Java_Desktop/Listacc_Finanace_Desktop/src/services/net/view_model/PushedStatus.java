/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net.view_model;

/**
 *
 * @author E-book
 */
public enum PushedStatus {
        False((short)0), True((short)1);
        
        private short value;
        //private static Map map = new HashMap<>();
        
        private PushedStatus(short value){
            this.value = value;
        }
        
        /*static {
            for(PushedStatus pushedStatus: PushedStatus.values()){
                map.put(pushedStatus.value, pushedStatus);
            }
        }
        
        public static PushedStatus valueOf(int pushedStatus) {
            return (PushedStatus) map.get(pushedStatus);
        }*/
        
        public short getValue() {
            return value;
        }
    }
