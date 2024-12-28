import json

# 원본 데이터를 저장한 텍스트 파일 경로
input_file = "cafe_data.txt"  # 기존 데이터를 저장한 파일 이름
output_file = "cafe_data.json"    # 저장할 JSON 파일 이름

# 텍스트 파일에서 데이터를 불러오기
with open(input_file, "r", encoding="utf-8") as file:
    data_content = file.read()

# 문자열 데이터를 Python 딕셔너리로 변환
data = eval(data_content)  # eval은 안전하지 않은 입력 데이터에는 사용하지 마세요.

# JSON 파일로 저장
with open(output_file, "w", encoding="utf-8") as file:
    json.dump(data, file, ensure_ascii=False, indent=4)

        
def clean_newlines(text):
    cleaned_text = text.split("\n")
    cleaned_text = [
        word for word in cleaned_text
        if not (
            word.startswith("영업") or
            word.endswith("종료") or
            word.endswith("시작") or
            word == "접기" or
            word.startswith("오더") or
            word.endswith("오더")
        )
    ]
    print(cleaned_text)
    day = ['월', '화', '수', '목', '금', '토', '일', '매일']
    new_text = []
    current_day = "임시"
    for word in cleaned_text:
        if word in day:
            current_day = word
        elif has_word_prefix(word, day):
            current_day = word[0]
        elif "휴무" in word:
            new_text.append(f"{current_day} {word}")
        elif ":" in word:
            new_text.append(f"{current_day} {word}")
        elif "운영시간" in word or "라스트오더" in word:
            new_text[-1] += f" ({word})"
    sorted = sort_by_day(new_text)
    return " ".join(sorted)
    # print(new_text)

def has_word_prefix(word, list):
    for l in list:
        if word.startswith(l):
            return True
    False
    
def sort_by_day(new_text):
    day_order = {'월': 1, '화': 2, '수': 3, '목': 4, '금': 5, '토': 6, '일': 7, '매일': 0}
    def get_day_order(item):
        for day in day_order.keys():
            if item.startswith(day):
                return day_order[day]
        return 100  # 우선순위에 없는 경우 맨 뒤로 이동
    # 정렬 
    sorted_text = sorted(new_text, key=get_day_order)
    return sorted_text  
  
with open(output_file) as file:
    json_data = json.load(file)
    # print(len(json_data))
    # print(json_data)
    for cafe_name in json_data:
        cafe_location = json_data[cafe_name][0]
        cafe_schedule = json_data[cafe_name][1]
        cafe_number = json_data[cafe_name][2]
        # print(type(cafe_schedule))
        cafe_new_schedule = clean_newlines(cafe_schedule)
        json_data[cafe_name] = [cafe_location, cafe_new_schedule, cafe_number]
    print(json_data)

filename = "cafe_data_cleaned.json"
with open(filename, "w", encoding="utf-8") as file:
    json.dump(json_data, file, ensure_ascii=False, indent=4)

# 크롤링이 완료된 후, 데이터 저장




# print(f"JSON 파일로 저장 완료: {output_file}")