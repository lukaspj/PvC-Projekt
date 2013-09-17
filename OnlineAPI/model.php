<?php
class App_model extends CI_Model {
	public function userexists($username)
	{
		$CI =& get_instance();
		$query = $CI->db->query("SELECT * FROM app_users WHERE username='$username'");
		if($query->num_rows() > 1)
			return -1;
		else
			return $query->num_rows() == 0 ? true : false;
	}
	//http://howto-use-mysql-spatial-ext.blogspot.dk/
	public function updatePosition($username, $lat, $long)
	{
		$CI =& get_instance();
		if($this->userexists($username))
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
}
/* END OF FILE */