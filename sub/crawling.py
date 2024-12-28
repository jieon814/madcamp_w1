import time
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from time import sleep
from bs4 import BeautifulSoup
import re
import json


opts = webdriver.ChromeOptions()
# options.add_argument("--headless")  # 브라우저를 표시하지 않음
opts.add_argument("--no-sandbox")
opts.add_argument("--disable-dev-shm-usage")


# url = "https://map.naver.com/p/search/%EB%8C%80%EC%A0%84%20%EC%B9%B4%ED%8E%98/place/1436786327?c=10.00,0,0,0,dh&placePath=%3Fentry%253Dpll"
url = "https://map.naver.com"
driver = webdriver.Chrome(options=opts)
driver.get(url)
key_word = '대전 카페'


def time_wait(num, code):
    rt = 0
    try:
        rt = WebDriverWait(driver, num).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, code)))
    except:
        print(code, "태그 못 찾음")
        driver.quit()
    return rt

time_wait(10, 'div.input_box > input.input_search')

def switch_frame(frame):
    driver.switch_to.default_content()
    driver.switch_to.frame(frame)

search = driver.find_element(By.CSS_SELECTOR, 'div.input_box > input.input_search')
search.send_keys(key_word)
search.send_keys(Keys.ENTER)

res = driver.page_source
soup = BeautifulSoup(res, 'html.parser') # ? html 파싱하여 가져온다는

sleep(1)


def page_down(num):
    body = driver.find_element(By.CSS_SELECTOR, 'body')
    body.click()
    for i in range(num):
        body.send_keys(Keys.PAGE_DOWN)
        time.sleep(0.5)
        # time_wait(5, 'li.UEzoS.rTjJo')

def page_up(num):
    body = driver.find_element(By.CSS_SELECTOR, 'body')
    body.click()
    for i in range(num):
        body.send_keys(Keys.PAGE_UP)
        # time.sleep(0.5)
        

switch_frame('searchIframe')
page_down(30)
sleep(2)

# cafe_list = driver.find_elements(By.CSS_SELECTOR,'div.CHC5F > a.tzwk0 > div > div > span.TYaxT')
# cafe_list = driver.find_elements(By.CSS_SELECTOR, 'li.UEzoS.rTjJo.cZnHG')
# print(len(cafe_list))
next_btn = driver.find_elements(By.CSS_SELECTOR, '.zRM9F> a')
print(len(next_btn))
page_up(30)
sleep(5)

cafe_dict = {}
start = time.time()
print('[------크롤링 시작------]')

processed_set = set()

for btn in range(len(next_btn))[1:]:
    time_wait(5, 'li.UEzoS.rTjJo')
    page_down(40)
    sleep(10)
    cafe_list = driver.find_elements(By.CSS_SELECTOR, 'div.CHC5F > a.tzwk0 > div > div > span.TYaxT')
    page = driver.find_elements(By.CSS_SELECTOR, 'li.UEzoS.rTjJo')
    # a = driver.find_element(By.CSS_SELECTOR, "div.Ryr1F").list
    # print(a, page)
    print("In new_page btn,", len(page), len(cafe_list))
    for data in range(len(cafe_list)):
        sleep(5)
        switch_frame('searchIframe')
        WebDriverWait(driver, 10).until(EC.presence_of_all_elements_located((By.CSS_SELECTOR, 'li.UEzoS.rTjJo')))
        page[data].click()
        try:
            sleep(3)
            # WebDriverWait(driver, 10).until(EC.presence_of_all_elements_located((By.CLASS_NAME, 'entryIframe')))
            switch_frame('entryIframe')
            time_wait(5, 'span.GHAhO')
            cafe_name = driver.find_element(By.CSS_SELECTOR, 'span.GHAhO').text
            if cafe_name in processed_set:
                continue
            processed_set.add(cafe_name)
            # print(cafe_name)

            WebDriverWait(driver, 5).until(EC.presence_of_element_located((By.XPATH, '//*[@id="app-root"]/div/div/div/div[4]/div/div/div/div/a[1]')))
            home = driver.find_element(By.XPATH, '//*[@id="app-root"]/div/div/div/div[4]/div/div/div/div/a[1]')
            home.click()

            time_wait(5, 'div.place_section_content > div > div.O8qbU.tQY7D > div > a > span.LDgIH')
            cafe_location = driver.find_element(By.CSS_SELECTOR, 'div.place_section_content > div > div.O8qbU.tQY7D > div > a > span.LDgIH' ).text
            # print(cafe_location)

            time_wait(5, 'div.place_section_content > div > div.O8qbU.pSavy > div > a')
            time_button = driver.find_element(By.CSS_SELECTOR, 'div.place_section_content > div > div.O8qbU.pSavy > div > a')
            time_button.click()
            time_schedule = time_button.text
            # print(time_schedule)
            # //*[@id="app-root"]/div/div/div/div[5]/div/div[2]/div[1]/div/div[2]/div/a/div/div/span/svg/path
            # //*[@id="app-root"]/div/div/div/div[5]/div/div[2]/div[1]/div/div[2]/div/a/div/div
            
            # time_wait(5, 'div.place_section_content > div > div.O8qbU.nbXkr > div > span.xlx7Q')
            try:
                rt = WebDriverWait(driver, 5).until(
                    EC.presence_of_element_located((By.CSS_SELECTOR, 'div.place_section_content > div > div.O8qbU.nbXkr > div > span.xlx7Q')))   
                call_num = driver.find_element(By.CSS_SELECTOR, 'div.place_section_content > div > div.O8qbU.nbXkr > div > span.xlx7Q').text
            except:
                # print("전화번호 없음")
                call_num = "-"
                
            #app-root > div > div > div > div:nth-child(5) > div > div:nth-child(2) > div.place_section_content > div > div.O8qbU.nbXkr > div > span.xlx7Q
            # print(call_num)

            cafe_dict[cafe_name] = [cafe_location, time_schedule, call_num]
            
            # driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        except Exception as e:
            # print(f"Error: {e}") 
            print("Oh...")
        
        if len(cafe_dict) == 50:
            print("All Done")
            break
            
        # switch_frame('searchIframe')
    if len(cafe_dict) == 50:
        break
    elif page[-1]:
        switch_frame('searchIframe')
        next_btn[-1].click()
        sleep(2)
    else:
        print("페이지 인식 못함")
        break


# 크롤링이 완료된 후, 데이터 저장
filename = "cafe_data.txt"

# txt 파일로 저장
with open(filename, "w", encoding="utf-8") as file:
    file.write(json.dumps(cafe_dict, ensure_ascii=False, indent=4))  # JSON 형식으로 저장

print(f"{len(cafe_dict)}개의 카페 데이터를 저장했습니다.")