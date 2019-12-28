namespace Listacc.Models
{
    public class Client
    {
        public int Id {get;set;}
        public string BusinessName {get;set;}
        public int Phone {get;set;}
        public string Email {get;set;}
        public string Address {get;set;}
        public string UID {get;set;}
        public string UID2 {get;set;}
        public double AmountReceivable {get;set;}
        public Person Person {get;set;}
    }
}