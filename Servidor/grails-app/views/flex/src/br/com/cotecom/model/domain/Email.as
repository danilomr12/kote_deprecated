package br.com.cotecom.model.domain{

	[RemoteClass(alias="br.com.cotecom.domain.Email")]
	[Bindable]
	public class Email{
		
		public var email:String
		
		public function Email(email:String = ""){
			this.email = email
		}

	}
}