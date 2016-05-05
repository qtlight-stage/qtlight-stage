//모든 메모는 여기에 위키 불편함
//메모에 대한 구상이 구체화되면 코드로 하던가 위키로 보내던가하셈

//예외처리 예시
		try {
			throw new Exception();
		}
		catch(Exception e) {
			System.out.println("catch\n");
		}
		finally {
			System.out.println("final\n");
		}
