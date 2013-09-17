<?php
class App_model extends CI_Model {
	public function usernameavailable($username)
	{
		$CI =& get_instance();
		$query = $CI->db->query("SELECT * FROM app_users WHERE username='$username'");
		if($query->num_rows() > 1)
			return -1;
		else if($query->num_rows() == 1)
			return false;
		else
			return true;
	}
	//http://howto-use-mysql-spatial-ext.blogspot.dk/
	public function updatePosition($username, $lat, $long)
	{
		$CI =& get_instance();
		if($this->usernameavailable($username))
		{
			$query = $CI->db->query("UPDATE app_users SET position=GeomFromText('POINT($lat $long)') WHERE username='$username'");
			return $CI->db->_error_number();
		}
		else
		{
			$query = $CI->db->query("INSERT INTO app_users (username, position) VALUES ('$username', GeomFromText('POINT($lat $long)'))");
			return $CI->db->_error_number();
		}
	}

	public function addUser($username, $password, $email)
    {
    	$CI =& get_instance();
		$data = array('username' => "".$username,
						'password' => "".$password,
						'email' => "".$email);
        $CI->db->insert('nn_users', $data);
		return $CI->db->_error_number();
	}
	
	public function getUser($username)
    {
    	$CI =& get_instance();
		$query = $CI->db->query("SELECT * FROM nn_users WHERE username='$username'");
	}
	
	public function verifyPass($username, $password, $updateFlags = true)
	{
		log_message("error", "MYSQL ERROR verifyPass: " . $username . "  -  " . $password);
		$CI =& get_instance();
		$query = $CI->db->query("SELECT * FROM nn_users WHERE username='$username' AND password='$password'");
		if($CI->db->_error_number() != 0)
			log_message("error", "MYSQL ERROR verifyPass: " . $CI->db->_error_message() );
		if($CI->db->_error_number() == 0){
			$result = $query->result();
			$CI->session->set_userdata('flags', $result[0]->flags);
			return TRUE;
		}
		else
			return FALSE;
	}
}
/* END OF FILE */