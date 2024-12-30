import time
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import json


opts = webdriver.ChromeOptions()
opts.add_argument("--no-sandbox")
opts.add_argument("--disable-dev-shm-usage")

# URL 설정
url = "https://map.naver.com"
driver = webdriver.Chrome(options=opts)
driver.get(url)
key_word = '대전 카페'

# 시간 대기 함수
def time_wait(num, code):
    try:
        WebDriverWait(driver, num).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, code)))
    except Exception as e:
        print(f"{code} 태그 못 찾음, 에러: {e}")
        driver.quit()

# 프레임 전환 함수
def switch_frame(frame):
    driver.switch_to.default_content()
    driver.switch_to.frame(frame)

# 페이지 다운 함수
def page_down(num):
    body = driver.find_element(By.CSS_SELECTOR, 'body')
    body.click()
    for _ in range(num):
        body.send_keys(Keys.PAGE_DOWN)
        time.sleep(0.5)

# 검색 수행
time_wait(10, 'div.input_box > input.input_search')
search = driver.find_element(By.CSS_SELECTOR, 'div.input_box > input.input_search')
search.send_keys(key_word)
search.send_keys(Keys.ENTER)

time.sleep(3)
switch_frame('searchIframe')
page_down(10)

# 버튼 및 데이터 초기화
next_btn = driver.find_elements(By.CSS_SELECTOR, '.zRM9F > a')
print(f"페이지 버튼 수: {len(next_btn)}")

cafe_dict = {}
processed_pages = set()
processed_set = set()

print('[------크롤링 시작------]')
start = time.time()

# 페이지 탐색
for btn_index in range(len(next_btn)):
    # 중복 방지용 처리
    time_wait(5, 'li.UEzoS.rTjJo')
    page_down(20)
    time.sleep(2)
    
    cafe_list = driver.find_elements(By.CSS_SELECTOR, 'div.CHC5F > a.tzwk0 > div > div > span.TYaxT')
    page_items = driver.find_elements(By.CSS_SELECTOR, 'li.UEzoS.rTjJo')

    print(f"페이지 {btn_index}: {len(page_items)}개의 항목, {len(cafe_list)}개의 카페 정보")

    for index, item in enumerate(page_items):
        # 중복된 페이지 건너뜀
        try:

            if not item.is_displayed():
                print(f"item {index}이 화면에 표시되지 않음. 다음으로 이동.")
                continue

            unique_id = item.get_attribute("data-laim-exp-id")
            if not unique_id:
                continue
            if unique_id in processed_pages:
                continue
            processed_pages.add(unique_id)
            
            # 항목 클릭
            switch_frame('searchIframe')
            WebDriverWait(driver, 10).until(EC.presence_of_all_elements_located((By.CSS_SELECTOR, 'li.UEzoS.rTjJo')))
            item.click()
            time.sleep(3)

            # 상세 정보 프레임 전환 및 데이터 수집
            switch_frame('entryIframe')
            time_wait(5, 'span.GHAhO')

            cafe_name = driver.find_element(By.CSS_SELECTOR, 'span.GHAhO').text
            if cafe_name in processed_set:
                continue
            processed_set.add(cafe_name)

            WebDriverWait(driver, 5).until(EC.presence_of_element_located((By.XPATH, '//*[@id="app-root"]/div/div/div/div[4]/div/div/div/div/a[1]')))
            home = driver.find_element(By.XPATH, '//*[@id="app-root"]/div/div/div/div[4]/div/div/div/div/a[1]')
            home.click()

            time_wait(5, 'div.place_section_content > div > div.O8qbU.tQY7D > div > a > span.LDgIH')
            location = driver.find_element(By.CSS_SELECTOR, 'div.place_section_content > div > div.O8qbU.tQY7D > div > a > span.LDgIH').text
            
            time_wait(5, 'div.place_section_content > div > div.O8qbU.pSavy > div > a')
            time_button = driver.find_element(By.CSS_SELECTOR, 'div.place_section_content > div > div.O8qbU.pSavy > div > a')
            time_button.click()
            time_schedule = time_button.text
            
            try:
                rt = WebDriverWait(driver, 5).until(
                    EC.presence_of_element_located((By.CSS_SELECTOR, 'div.place_section_content > div > div.O8qbU.nbXkr > div > span.xlx7Q')))   
                call_num = driver.find_element(By.CSS_SELECTOR, 'div.place_section_content > div > div.O8qbU.nbXkr > div > span.xlx7Q').text
            except:
                call_num = "전화번호 없음"

            cafe_dict[cafe_name] = [location, time_schedule, call_num]
            # print(f"수집 완료: {cafe_name}, {location}, {call_num}")

        except Exception as e:
            print(f"데이터 수집 중 에러 발생: {e}")

        # 데이터 50개 수집 후 종료
        if len(cafe_dict) >= 50:
            break

    # 데이터 50개 수집 후 종료
    if len(cafe_dict) >= 50:
        print("50개의 데이터를 수집하였습니다.")
        break

    # 다음 페이지 버튼 클릭
    if btn_index < len(next_btn) - 1:
        switch_frame('searchIframe')
        next_btn[btn_index + 1].click()
        time.sleep(3)
    else:
        print("더 이상 페이지가 없습니다.")
        break

# 크롤링 완료 후 파일 저장
filename = "cafe_data.txt"
with open(filename, "w", encoding="utf-8") as file:
    file.write(json.dumps(cafe_dict, ensure_ascii=False, indent=4))

print(f"{len(cafe_dict)}개의 카페 데이터를 {filename}에 저장했습니다.")
print(f"총 소요 시간: {time.time() - start}초")
driver.quit()


